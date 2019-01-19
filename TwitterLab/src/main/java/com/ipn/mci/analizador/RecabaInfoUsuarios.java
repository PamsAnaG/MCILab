/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.infraestructura.comun.bd.Registro;
import com.ipn.mci.analizador.dao.CargaDatosDAO;
import com.ipn.mci.analizador.domain.UsuarioTw;
import com.ipn.mci.twitterlab.UserTimelineConsumer;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Pam
 */
public class RecabaInfoUsuarios {

    public void recabaInfoUsuarios() {

        CargaDatosDAO datos = new CargaDatosDAO();
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {
            // CARGAMOS LOS USUARIOS QUE ESTAN ALOJADOS EN BD PARA CARGAR LA INFORMACION COMPLEMENTARIA            
            datos.abreConexion("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mcitesis?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Desarrollo");

            UserTimelineConsumer twConsumer = new UserTimelineConsumer(
                    "161048591-KxCYXE0gXQUbmnhOLjot4J9LMcTqbrcSS9l2lelo",
                    "p4tqX2mpRppuILzYCmdex5RO08Zug47OtSSTX9raHuFgx",
                    "1otukjBXtwAkNkC0m1UwtovIF",
                    "F2MGdRatio9REWzQR5uisNIPtOW1Mno7S2PV5UeZx3CkabvNoh",
                    "https://api.twitter.com/1.1/statuses/user_timeline.json", "");

            LinkedList usuarios = datos.getUsuariosNoInfo();
            Iterator usuariosIt = usuarios.iterator();
            int contadorUsuarios = 0;
            while (usuariosIt.hasNext()) {
                UsuarioTw usuario = (UsuarioTw) usuariosIt.next();
                System.out.println("Obteniendo informacion de: " + usuario.getScreenName());

                contadorUsuarios++;
                //twConsumer.setScreename((String)usuario.getDefCampo("SCREEN_NAME"));
                twConsumer.setScreename(usuario.getScreenName());
                String timeLine = twConsumer.consumeTimeLine();
                fichero = new FileWriter("/Users/Pam/Documents/MCI/Tesis/Proyectos/ArchivosTwitter/Usuarios/" + usuario.getScreenName() + ".json");
                pw = new PrintWriter(fichero);
                pw.println(timeLine);
                datos.actualizaUsuarioInfo(Integer.valueOf(usuario.getIdUsuarioTw()));
                if (contadorUsuarios == 1) {
                    break;
                }

            }

        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            if (datos.isConectado()) {
                datos.cierraConexion();
            }
            if (fichero != null) {
                try {
                    fichero.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static void main(String[] arg) {
        RecabaInfoUsuarios servicio = new RecabaInfoUsuarios();
        servicio.recabaInfoUsuarios();
    }

}
