/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pam
 */
public class Analizador {

    private AnalizaTweets analizaTweets = new AnalizaTweets();

    public void analizaArchivo(String directorioPrincipal) {

        try {
            SimpleDateFormat formatoCarpeta = new SimpleDateFormat("ddMMyyyy");
            if (!directorioPrincipal.equals("")) {
                File carpeta = new File(directorioPrincipal);
                if (carpeta.exists()) {
                    File[] directorios = carpeta.listFiles();
                    for (File directorio : directorios) {
                        // SOLO ANALIZAMOS DIRECTORIOS
                        if (directorio.isDirectory()) {
                            List<String> tweets = new ArrayList();
                            // VERIFICAMOS QUE EL NOMBRE DE LA CARPETA SEA CORRECTO
                            System.out.println("\nRecorriendo directorio " + directorio.getName());
                            Date fechaCarpeta = formatoCarpeta.parse(directorio.getName());
                            File[] archivos = directorio.listFiles();
                            for (File archivo : archivos) {
                                if (!archivo.getName().equals(".DS_Store")) {
                                    System.out.println("Analizando archivo " + archivo.getName());
                                    FileReader f = new FileReader(archivo);
                                    BufferedReader b = new BufferedReader(f);
                                    String linea;
                                    while ((linea = b.readLine()) != null) {
                                        if (!linea.isEmpty()) {
                                            tweets.add(linea);
                                        }
                                    }
                                    b.close();
                                }
                            }
                            // ANALIZAMOS LOS TWEETS ENCONTRADOS                    
                            analizaTweets.analizaTweets(tweets, fechaCarpeta);
                        }
                    }
                }
            }

        } catch (Exception excp) {
            excp.printStackTrace();
        }

    }

    public static void main(String arg[]) {
        Analizador analizador = new Analizador();
        analizador.analizaArchivo("/Users/Pam/Documents/MCI/Tesis/Proyectos/ArchivosTwitter/Ejemplo/");
    }

}
