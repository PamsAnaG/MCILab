/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.ipn.mci.analizador.dao.FiltraResultadosDAO;
import com.ipn.mci.analizador.domain.AnalisisSentimiento;
import com.ipn.mci.analizador.domain.ConteoAnalisis;
import com.ipn.mci.analizador.domain.ResultadosAnalisis;
import com.ipn.mci.analizador.domain.TipoConteo;
import com.ipn.mci.analizador.domain.UsuarioTw;
import com.ipn.mci.nlp.NLProcessor;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Word;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Pam
 */
public class FiltraResultados {

    private HashMap<String, Integer> afinnLexicon = new HashMap<String, Integer>();

    public void filtraResultados() {

        // PRIMERO VEREMOS CUANTOS DIAS TENEMOS DISPONIBLES PARA HACER EL ANALISIS
        FiltraResultadosDAO datos = new FiltraResultadosDAO();

        try {
            datos.abreConexion("com.mysql.cj.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mcitesis?useUnicode=true&useTimezone=true", "root", "Desarrollo");

            cargaAFINNLEXICON();
            List<TipoConteo> tipoConteo = new ArrayList();
            tipoConteo.add(TipoConteo.PALABRA);
            tipoConteo.add(TipoConteo.HASHTAG);
            tipoConteo.add(TipoConteo.USUARIO);
            List<ConteoAnalisis> conteosEfectivos = new ArrayList();
            LinkedList<ResultadosAnalisis> fechas = datos.getDiasAnalisis();
            Iterator fechasIt = fechas.iterator();
            while (fechasIt.hasNext()) {
                // PROCESAMOS PRIMERO LAS PALABRAS PARA LA FECHA DE ANALISIS
                ResultadosAnalisis fechaAnalisis = (ResultadosAnalisis) fechasIt.next();
                for (TipoConteo tipo : tipoConteo) {
                    System.out.println("Procesando " + tipo.toString() + "|" + fechaAnalisis.getFhAnalisis());
                    LinkedList<ConteoAnalisis> palabrasAnalisis = datos.getRegistrosDias(fechaAnalisis.getIdResultadosAnalisis(), tipo.ordinal());
                    for (ConteoAnalisis conteoAnalisis : palabrasAnalisis) {
                        // CALCULAMOS EL 20% DE LOS USUARIOS QUE MENCIONARON LA PALABRA
                        int usuariosEfectivos = 0;
                        double usuariosPorcentaje = conteoAnalisis.getListaUsuarios().size() * 0.20;
                        double usuariosPorcentajInteraccion = conteoAnalisis.getListaUsuarios().size() * 0.20;
                        int usuariosInteraccion = 0;
                        System.out.println(conteoAnalisis.getItem() + " Usuario " + conteoAnalisis.getListaUsuarios().size());
                        for (UsuarioTw usuario : conteoAnalisis.getListaUsuarios()) {
                            if (usuario.getTffRatio() > 1) {
                                usuariosEfectivos++;
                            }
                            // INTERACCION DE AL MENOS 10% DE SU AUDIENCIA EN LOS ULTIMOS 50 TWEETS
                            double porcentajeAudiencia = usuario.getFollowers() * 0.10;
                            int interaccionUsuario = usuario.getReTweets() + usuario.getLikes();
                            if (interaccionUsuario > porcentajeAudiencia) {
                                usuariosInteraccion++;
                            }
                            // VALIDACION DE TFF RATIO
                            if (usuariosEfectivos > usuariosPorcentaje) {
                                if (usuariosInteraccion > usuariosPorcentajInteraccion) {
                                    conteosEfectivos.add(conteoAnalisis);
                                    break;
                                }
                            }
                        }
                    }

                    System.out.println("---------------------------------------------");
                    for (ConteoAnalisis conteo : conteosEfectivos) {
                        System.out.println(conteo.getItem() + " Usuario " + conteo.getListaUsuarios().size());
                    }

                    System.out.println("---------------------------------------------");
                    List<AnalisisSentimiento> listaAnalisis = analisisSentimientos(conteosEfectivos);
                    for (AnalisisSentimiento analisis : listaAnalisis) {
                        datos.insertaResultadoFinal(analisis);
                    }
                    datos.actualizaUsuarioInfo(fechaAnalisis.getIdResultadosAnalisis());
                    conteosEfectivos = new ArrayList();
                    System.out.println("************************************************");

                }
            }
        } catch (Exception excp) {
            excp.printStackTrace();
        }

    }

    private List<AnalisisSentimiento> analisisSentimientos(List<ConteoAnalisis> conteos) {

        List<AnalisisSentimiento> analisisList = new ArrayList();

        for (ConteoAnalisis conteo : conteos) {
            List<String> tweets = new ArrayList();
            AnalisisSentimiento analisis = new AnalisisSentimiento();
            for (UsuarioTw usuario : conteo.getListaUsuarios()) {
                tweets.add(usuario.getTweetTexto());
                String[] words = usuario.getTweetTexto().split("\\s");
                //System.out.println("Analizando " + usuario.getTweetTexto());
                for (String word : words) {
                    if (afinnLexicon.containsKey(limpiaPalabra(word))) {
                        analisis.setRatingGeneral(analisis.getRatingGeneral() + afinnLexicon.get(limpiaPalabra(word)));
                        if (afinnLexicon.get(limpiaPalabra(word)) < 0) {
                            analisis.setConteoNegativo(analisis.getConteoNegativo() + 1);
                            //System.out.println("Palabra negativa encontrada " + word);
                        } else {
                            analisis.setConteoPositivo(analisis.getConteoPositivo() + 1);
                            //System.out.println("Palabra positiva encontrada " + word);
                        }
                    }
                }
            }
            // HACEMOS ANALISIS DE SENTIMIENTOS CON FREELING          
            AnalisisSentimiento analisisF = analisisSentimientosF(tweets);
            analisis.setConteoNegativoF(analisisF.getConteoNegativoF());
            analisis.setConteoPositivoF(analisisF.getConteoPositivoF());
            analisis.setRatingGeneralF(analisisF.getRatingGeneralF());
            analisis.setIdConteoDia(conteo.getIdConteoDia());
            System.out.println(conteo.getItem() + " | " + analisis.getRatingGeneral() + " | Negativo " + analisis.getConteoNegativo() + " | Positivo " + analisis.getConteoPositivo());
            analisisList.add(analisis);
        }
        return analisisList;
    }

    private AnalisisSentimiento analisisSentimientosF(List<String> tweets) {

        NLProcessor nlProcessor = new NLProcessor();
        AnalisisSentimiento analisis = new AnalisisSentimiento();

        List<ListSentence> sentences = nlProcessor.getTaggerResults(tweets);

        for (ListSentence sentencesList : sentences) {

            ListSentenceIterator sIt = new ListSentenceIterator(sentencesList);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();                
                ListWordIterator wIt = new ListWordIterator(s);
                while (wIt.hasNext()) {
                    Word word = wIt.next();
                    // BUSCAMOS EL ADJETIVO Y EL ADVERBIO DE LA ORACION
                    if (word.getTag().charAt(0) == 'A' || word.getTag().charAt(0) == 'R') {
                        // BUSCAMOS EL LEMMA DENTRO DEL DICCIONARIO                         
                        if (afinnLexicon.containsKey(limpiaPalabra(word.getLemma()))) {
                            analisis.setRatingGeneralF(analisis.getRatingGeneralF() + afinnLexicon.get(limpiaPalabra(word.getLemma())));
                            if (afinnLexicon.get(limpiaPalabra(word.getLemma())) < 0) {
                                analisis.setConteoNegativoF(analisis.getConteoNegativoF() + 1);
                                //System.out.println(word.getForm() + " " + word.getLemma() + " " + word.getTag() + " Negativo");
                            } else {
                                analisis.setConteoPositivoF(analisis.getConteoPositivoF() + 1);
                                //System.out.println(word.getForm() + " " + word.getLemma() + " " + word.getTag() + " Positivo");
                            }
                        }
                    }
                }
            }
        }
        System.out.println(analisis.getRatingGeneralF() + " | Negativo " + analisis.getConteoNegativoF() + " | Positivo " + analisis.getConteoPositivoF());

        return analisis;
    }

    private String limpiaPalabra(String palabra) {
        String paralabraProcesable = "";
        paralabraProcesable = palabra.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "u");
        // MANEJAMOS TODAS LAS PALABRAS EN MAYUSCULAS
        return paralabraProcesable.toUpperCase();
    }

    private void cargaAFINNLEXICON() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/Pam/Documents/MCI/Tesis/Proyectos/MCILab/AFINNLexicon"), "UTF8"));) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = in.readLine()) != null) {
                if (!primeraLinea) {
                    // ESTANDARIZAR A MAYUSCULAS
                    String[] partesLinea = linea.split(",");
                    afinnLexicon.put(limpiaPalabra(partesLinea[0]), Integer.valueOf(partesLinea[1]));
                }
                primeraLinea = false;
            }
        } catch (Exception excp) {
            excp.printStackTrace();

        }

    }

    public static void main(String arg[]) {
        FiltraResultados servicio = new FiltraResultados();
        servicio.filtraResultados();

        //List<String> textos = new ArrayList();
        //textos.add("La muerte es relativa solo es la culminación de nuestro paso sobre la tierra realmente muere quien no es recordado #DíaDeMuertos #DiaDeMuertosMexico httpstcoM0BM79ow9w");
        //textos.add("Afortunadamente existen otras opciones no Si Samsung y Apple van a seguir con lo mismo lo mejor es ir con una tercera opción que no haga eso Hace unos dias Huawei se pronunció al respecto pero existen otros tantos");
        //servicio.analisisSentimientosF(textos);
    }

}
