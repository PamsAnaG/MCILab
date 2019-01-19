/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

/**
 *
 * @author Pam
 */
public class AnalizaInfoUsuarios {

    public void leeInfoUsuarios(String directorioPrincipal) {

        try {
            if (!directorioPrincipal.equals("")) {
                File carpeta = new File(directorioPrincipal);
                if (carpeta.exists()) {
                    StringBuilder tweetsUsuario = new StringBuilder();
                    File[] archivos = carpeta.listFiles();
                    for (File archivo : archivos) {
                        if (!archivo.getName().equals(".DS_Store")) {
                            System.out.println("Analizando archivo " + archivo.getName());
                            FileReader f = new FileReader(archivo);
                            BufferedReader b = new BufferedReader(f);
                            String linea;
                            while ((linea = b.readLine()) != null) {
                                if (!linea.isEmpty()) {
                                    tweetsUsuario.append(linea);
                                }
                            }
                            b.close();
                            // ANALIZAMOS LOS TWEETS DEL USUARIO
                            analizaInfoUsuarios(archivo.getName().replace(".json", ""), tweetsUsuario.toString());
                        }
                    }
                }
            }
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }

    private void analizaInfoUsuarios(String screenName, String tweetsUsuario) {

        System.out.println("Analizando informacion de " + screenName);
        JsonParser parser = new JsonParser();
        JsonArray tweets = (JsonArray) parser.parse(tweetsUsuario).getAsJsonArray();
        System.out.println("Tweets a analizar " + tweets.size());
        Iterator tweetsIt = tweets.iterator();
        while (tweetsIt.hasNext()) {
            JsonElement tweet = (JsonElement) tweetsIt.next();
            JsonObject tweetJ = tweet.getAsJsonObject();
            String tweetTexto = "";
            if (tweetJ.get("truncated").getAsBoolean()) {
                JsonObject tweetExtendido = tweetJ.get("extended_tweet").getAsJsonObject();
                tweetTexto = tweetExtendido.get("full_text").getAsString();
            } else {
                tweetTexto = tweetJ.get("text").getAsString();
            }
            System.out.println(tweetTexto);
        }
    }

    public static void main(String arg[]) {
        AnalizaInfoUsuarios analizador = new AnalizaInfoUsuarios();
        analizador.leeInfoUsuarios("/Users/Pam/Documents/MCI/Tesis/Proyectos/ArchivosTwitter/Usuarios/");
    }

}
