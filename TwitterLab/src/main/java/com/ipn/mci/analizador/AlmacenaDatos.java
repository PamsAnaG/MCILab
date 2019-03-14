/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.infraestructura.comun.bd.Conexion4;
import com.infraestructura.comun.bd.Registro;
import com.ipn.mci.analizador.dao.AlamacenaDatosDAO;
import com.ipn.mci.analizador.dao.CargaDatosDAO;
import com.ipn.mci.analizador.domain.ConteoAnalisis;
import com.ipn.mci.analizador.domain.ResultadosAnalisis;
import com.ipn.mci.analizador.domain.Tweet;
import com.ipn.mci.analizador.domain.UsuarioTw;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Pam
 */
public class AlmacenaDatos {

    private HashMap usuariosBD = new HashMap();

    public void almacenaDatos(ResultadosAnalisis resultados) {
        AlamacenaDatosDAO datos = new AlamacenaDatosDAO();

        try {

            datos.abreConexion("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mcitesis?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Desarrollo");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Fecha registro " + resultados.getFhRegistro());
            System.out.println("Fecha analisis " + resultados.getFhAnalisis());
            System.out.println("Tweets analizados " + resultados.getTweetsAnalizados());
            System.out.println("Palabras promedio por tweet " + resultados.getPromedioPalabraTweet());
            System.out.println("Palabras " + resultados.getNumeroPalabras());
            System.out.println("Palabras únicas " + resultados.getNumeroPalabrasUnicas());
            System.out.println("Palabras limpias " + resultados.getNumeroPalabrasLimpias());
            System.out.println("Palabras limpias unicas " + resultados.getNumeroPalabrasLimpiasUnicas());
            System.out.println("Diversidad lexica " + resultados.getDivLexica());
            System.out.println("----------------------------------------------------------------------------------");

            int claveResultados = datos.insertaResultados(resultados);
            cargaUsuariosProcesados(datos);

            // INSERTAMOS LA INFORMACION DE LOS USUARIOS            
            Set<String> userNames = resultados.getUsuarios().keySet();
            for (String userScreenName : userNames) {
                if (!usuariosBD.containsKey(userScreenName)) {
                    UsuarioTw usuarioTw = resultados.getUsuarios().get(userScreenName);
                    usuarioTw.setIdUsuarioTw(datos.insertaUsuario(usuarioTw));
                }
            }

            Iterator conteoIt = resultados.getConteoAnalisis().iterator();
            while (conteoIt.hasNext()) {
                ConteoAnalisis conteo = (ConteoAnalisis) conteoIt.next();
                conteo.setIdConteoDia(datos.insertaConteo(claveResultados, conteo));
                for (UsuarioTw usuario : conteo.getListaUsuarios()) {
                    //System.out.println(conteo.getItem() + "|" + usuario.getScreenName() + "|" + usuario.getTweetTexto());
                    int idTweet = datos.insertaMencionadaEn(usuario.getTweetTexto());
                    datos.insertaConteoUsuario(resultados.getUsuarios().get(usuario.getScreenName()), idTweet, conteo);
                }
                
                
            }

        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            if (datos.isConectado()) {
                datos.cierraConexion();
            }
        }
    }

    private void cargaUsuariosProcesados(Conexion4 conexion) throws Exception {
        CargaDatosDAO datos = new CargaDatosDAO();

        try {

            datos.setConexion(conexion.getConexion());

            LinkedList usuarios = datos.getUsuarios();
            Iterator usuIt = usuarios.iterator();
            while (usuIt.hasNext()) {
                UsuarioTw usuario = (UsuarioTw) usuIt.next();
                usuariosBD.put(usuario.getScreenName(), usuario);
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
