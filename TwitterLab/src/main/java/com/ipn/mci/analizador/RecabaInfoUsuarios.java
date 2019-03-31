/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                twConsumer.setScreename(usuario.getScreenName());
                String timeLine = twConsumer.consumeTimeLine();
                procesaInfoUsuario(timeLine, usuario);
                datos.actualizaUsuarioInfo(usuario);

                if (contadorUsuarios == 450) {
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

    public boolean procesaInfoUsuario(String timeLine, UsuarioTw usuario) {

        try {

            JsonParser parser = new JsonParser();
            JsonArray timeLineJSON = parser.parse(timeLine).getAsJsonArray();
            for (int i = 0; i < timeLineJSON.size(); i++) {
                JsonObject tweet = timeLineJSON.get(i).getAsJsonObject();
                // Solo contamos los tweets propios del usuario, no los que haya retweeteado y que tienen un reteet count desde origen
                if (tweet.get("retweeted_status") == null) {
                    if (tweet != null) {
                        usuario.setReTweets(usuario.getReTweets() + tweet.get("retweet_count").getAsInt());
                        usuario.setLikes(usuario.getLikes() + tweet.get("favorite_count").getAsInt());
                    }
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(timeLineJSON);

            FileWriter writer = new FileWriter("/Users/Pam/Documents/MCI/Tesis/Proyectos/ArchivosTwitter/Usuarios/" + usuario.getScreenName() + ".json");
            writer.write(json);
            writer.close();

            return true;

        } catch (Exception excp) {
            System.out.println("Informacion de usuario incorrecta: " + usuario.getScreenName());
        }
        return false;

    }

    public static void main(String[] arg) {
        RecabaInfoUsuarios servicio = new RecabaInfoUsuarios();
        servicio.recabaInfoUsuarios();
    }

}
