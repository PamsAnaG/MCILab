/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infraestructura.comun.bd.Registro;
import com.ipn.mci.analizador.dao.CargaDatosDAO;
import com.ipn.mci.analizador.domain.ConteoAnalisis;
import com.ipn.mci.analizador.domain.DatosPalabra;
import com.ipn.mci.analizador.domain.ResultadosAnalisis;
import com.ipn.mci.analizador.domain.TipoConteo;
import com.ipn.mci.analizador.domain.Tweet;
import com.ipn.mci.analizador.domain.UsuarioTw;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Pam
 */
public class AnalizaTweets {

    private Set<String> palabrasVacias = new HashSet<String>();

    private ResultadosAnalisis resultados = new ResultadosAnalisis();

    public void analizaTweets(List<String> tweetsJSON, Date fecha) {

        resultados = null;
        resultados = new ResultadosAnalisis();

        HashMap<String, Integer> palabras = new HashMap();
        HashMap<String, Integer> palabrasLimpias = new HashMap();
        HashMap<String, List<UsuarioTw>> palabrasLimpiasDatos = new HashMap();
        HashMap<Integer, String> retweets = new HashMap();
        long totalPalabras = 0;

        try {
            cargapalabrasVacias();
            // CARGAMOS LOS USUARIOS QUE TENEMOS YA REISTRADOS
            cargaUsuariosProcesados();
            JsonParser parser = new JsonParser();
            for (String tweetJSON : tweetsJSON) {
                try {
                    JsonObject tweet = parser.parse(tweetJSON).getAsJsonObject();

                    String tweetTexto = "";
                    if (tweet.get("truncated").getAsBoolean()) {
                        JsonObject tweetExtendido = tweet.get("extended_tweet").getAsJsonObject();
                        tweetTexto = tweetExtendido.get("full_text").getAsString();
                    } else {
                        tweetTexto = tweet.get("text").getAsString();
                    }
                    tweetTexto = tweetTexto.replaceAll("[.,?\\/!$%\\^&\\*;:{}=\\-_`~()”“\"…]", "");
                    tweetTexto = tweetTexto.replaceAll("[\\s]+", " ");
                    if (tweet.get("retweet_count").getAsInt() > 0) {
                        retweets.put(tweet.get("retweet_count").getAsInt(), tweetTexto);
                    }

                    // OBTENEMOS LA INFORMACION DEL USUARIO
                    JsonObject usuarioTw = tweet.get("user").getAsJsonObject();
                    UsuarioTw usuario = new UsuarioTw();
                    usuario.setFollowers(usuarioTw.get("followers_count").getAsInt());
                    usuario.setFriends(usuarioTw.get("friends_count").getAsInt());
                    usuario.setLists(usuarioTw.get("listed_count").getAsInt());
                    usuario.setName(usuarioTw.get("name").getAsString());
                    usuario.setScreenName(usuarioTw.get("screen_name").getAsString());
                    usuario.setTweets(usuarioTw.get("statuses_count").getAsInt());
                    usuario.setUserId(usuarioTw.get("id_str").getAsString());
                    usuario.setTffRatio(usuario.getFollowers() / usuario.getFriends());
                    usuario.setTweetTexto(tweetTexto);

                    // ACTUALIZAMOS EL USUARIO SI ES QUE YA ESTA EN LA BASE
                    if (!resultados.getUsuarios().containsKey(usuario.getScreenName())) {
                        resultados.getUsuarios().put(usuario.getScreenName(), usuario);
                    }

                    // ELIMINAR PALABRAS VACIAS                
                    String[] words = tweetTexto.split("\\s");
                    for (String word : words) {
                        totalPalabras++;
                        word = limpiaPalabra(word);
                        palabras.put(word, ((palabras.containsKey(word)) ? palabras.get(word) + 1 : 1));
                        if (!palabrasVacias.contains(word)) {
                            palabrasLimpias.put(word, ((palabrasLimpias.containsKey(word)) ? palabrasLimpias.get(word) + 1 : 1));
                            if (palabrasLimpiasDatos.containsKey(word)) {
                                palabrasLimpiasDatos.get(word).add(usuario);
                            } else {
                                List<UsuarioTw> usuariosL = new ArrayList();
                                usuariosL.add(usuario);
                                palabrasLimpiasDatos.put(word, usuariosL);
                            }
                        }
                    }
                } catch (Exception excp) {
                    System.out.println("Descartando tweet " + tweetJSON);
                }
            }

            // ORDENAMOS LAS PALABRAS, HASHTAGS Y USUARIOS ENCONTRADOS
            Map<String, Integer> palabrasOrdenadas = palabrasLimpias.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            DecimalFormat formato = new DecimalFormat("#.000");
            resultados.setFhAnalisis(fecha);
            resultados.setFhRegistro(new Date());
            resultados.setPromedioPalabraTweet(formato.parse(String.valueOf(totalPalabras / tweetsJSON.size())).floatValue());
            resultados.setTweetsAnalizados(tweetsJSON.size());
            resultados.setNumeroPalabras(totalPalabras);
            resultados.setNumeroPalabrasUnicas(palabras.keySet().size());

            // LEXICAL DIVERSITY
            Iterator entryIt = palabrasLimpias.keySet().iterator();
            int numeroPalabrasLimpias = 0;
            while (entryIt.hasNext()) {
                String key = (String) entryIt.next();
                numeroPalabrasLimpias += palabrasLimpias.get(key);
            }
            resultados.setNumeroPalabrasLimpias(numeroPalabrasLimpias);
            resultados.setNumeroPalabrasLimpiasUnicas(palabrasLimpias.keySet().size());

            double divLexica = (Double.valueOf(resultados.getNumeroPalabrasUnicas()) / Double.valueOf(resultados.getNumeroPalabras())) * 100;
            resultados.setDivLexica(divLexica);

            Set palabrasK = palabrasOrdenadas.keySet();
            Iterator palabrasKIt = palabrasK.iterator();
            int usuarios = 1;
            int hashtag = 1;
            int palabrasN = 1;
            int lugarPalabras = 1;
            int lugarHashT = 1;
            int lugarUsuario = 1;
            while (palabrasKIt.hasNext()) {
                String key = (String) palabrasKIt.next();
                ConteoAnalisis conteo = new ConteoAnalisis();
                conteo.setConteoItem(palabras.get(key));
                conteo.setFhRegistro(new Date());
                conteo.setItem(key);
                if (key.startsWith("@") && usuarios <= 10) { // USUARIO
                    usuarios++;
                    conteo.setTipoConteo(TipoConteo.USUARIO);
                    conteo.setListaUsuarios(palabrasLimpiasDatos.get(key));
                    conteo.setLugar(lugarPalabras++);                    
                    resultados.getConteoAnalisis().add(conteo);
                } else if (key.startsWith("#") && hashtag <= 10) { // HASHTAG
                    hashtag++;
                    conteo.setTipoConteo(TipoConteo.HASHTAG);
                    conteo.setListaUsuarios(palabrasLimpiasDatos.get(key));
                    conteo.setLugar(lugarHashT++);                    
                    resultados.getConteoAnalisis().add(conteo);
                } else if (!key.startsWith("https") && palabrasN <= 15) { // PALABRA
                    palabrasN++;
                    conteo.setTipoConteo(TipoConteo.PALABRA);
                    conteo.setListaUsuarios(palabrasLimpiasDatos.get(key));
                    conteo.setLugar(lugarUsuario++);                    
                    resultados.getConteoAnalisis().add(conteo);
                }
                if (usuarios == 10 && hashtag == 10 && palabrasN == 10) {
                    break;
                }
            }

            int retweetsC = 0;
            Iterator keysRet = retweets.keySet().iterator();
            while (keysRet.hasNext()) {
                retweetsC++;
                Integer key = (Integer) keysRet.next();
                if (retweetsC <= 10) {
                    ConteoAnalisis conteo = new ConteoAnalisis();
                    conteo.setConteoItem(key);
                    conteo.setFhRegistro(new Date());
                    conteo.setItem(retweets.get(key));
                    conteo.setTipoConteo(TipoConteo.RETWEET);
                    resultados.getConteoAnalisis().add(conteo);
                }

            }
            AlmacenaDatos almacena = new AlmacenaDatos();
            almacena.almacenaDatos(resultados);

        } catch (Exception excp) {
            excp.printStackTrace();

        }
    }

    private String limpiaPalabra(String palabra) {
        String paralabraProcesable = "";
        paralabraProcesable = palabra.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "u");
        // MANEJAMOS TODAS LAS PALABRAS EN MAYUSCULAS
        return paralabraProcesable.toUpperCase();
    }

    private void cargapalabrasVacias() {
        try (FileReader fr = new FileReader("/Users/Pam/Documents/MCI/Tesis/Proyectos/MCILab/stopwords.txt")) {
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                // ESTANDARIZAR A MAYUSCULAS
                palabrasVacias.add(linea.toUpperCase());
            }
        } catch (Exception excp) {
            excp.printStackTrace();

        }
    }

    private void cargaUsuariosProcesados() throws Exception {
        CargaDatosDAO datos = new CargaDatosDAO();

        try {
            datos.abreConexion("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mcitesis?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Desarrollo");

            LinkedList usuarios = datos.getUsuarios();
            Iterator usuIt = usuarios.iterator();
            while (usuIt.hasNext()) {
                UsuarioTw usuario = (UsuarioTw) usuIt.next();

                resultados.getUsuarios().put(usuario.getScreenName(), usuario);

            }

        } catch (Exception excp) {
            excp.printStackTrace();
            throw new Exception(excp.getMessage());
        } finally {
            if (datos.isConectado()) {
                datos.cierraConexion();
            }

        }
    }
}
