/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.twitterlab;

/**
 *
 * @author Pam
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class TwitterConsumer extends Thread {

    static String STORAGE_DIR = "/Users/Pam/Documents/MCI/Tesis/Proyectos/ArchivosTwitter";
    static long BYTES_PER_FILE = 64 * 1024 * 1024;
    public long Messages = 0;
    public long Bytes = 0;
    public long Timestamp = 0;

    private String accessToken = "";
    private String accessSecret = "";
    private String consumerKey = "";
    private String consumerSecret = "";

    private String feedUrl;
    private String filePrefix;
    boolean isRunning = true;
    File file = null;
    FileWriter fw = null;
    long bytesWritten = 0;

    public static void main(String[] args) {
        TwitterConsumer t = new TwitterConsumer(
                "161048591-KxCYXE0gXQUbmnhOLjot4J9LMcTqbrcSS9l2lelo",
                "p4tqX2mpRppuILzYCmdex5RO08Zug47OtSSTX9raHuFgx",
                "1otukjBXtwAkNkC0m1UwtovIF",
                "F2MGdRatio9REWzQR5uisNIPtOW1Mno7S2PV5UeZx3CkabvNoh",
                "https://stream.twitter.com/1.1/statuses/filter.json?language=es&locations=-99.294674,19.188564,-98.961779,19.590641", "tweets");
        t.start();
    }

    public TwitterConsumer(String accessToken, String accessSecret, String consumerKey, String consumerSecret, String url, String prefix) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        feedUrl = url;
        filePrefix = prefix;
        Timestamp = System.currentTimeMillis();
    }

    /**
     * @throws IOException
     */
    private void rotateFile() throws IOException {
        // Handle the existing file
        if (fw != null) {
            fw.close();
        }
        SimpleDateFormat formatoArchivo = new SimpleDateFormat("ddMMyyyyhhmmss");
        file = new File(STORAGE_DIR, filePrefix + "-" + formatoArchivo.format(new Date()) + ".json");
        bytesWritten = 0;
        fw = new FileWriter(file);
        System.out.println("Writing to " + file.getAbsolutePath());
    }

    /**
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
            rotateFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (isRunning) {
            try {

                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
                consumer.setTokenWithSecret(accessToken, accessSecret);
                HttpGet request = new HttpGet(feedUrl);
                consumer.sign(request);

                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while (true) {
                    String line = reader.readLine();
                    System.out.println(line);
                    if (line == null) {
                        break;
                    }
                    if (line.length() > 0) {
                        if (bytesWritten + line.length() + 1 > BYTES_PER_FILE) {
                            rotateFile();
                        }
                        fw.write(line + "\n");
                        bytesWritten += line.length() + 1;
                        Messages++;
                        Bytes += line.length() + 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Esperando antes de reconectar...");
            try {
                Thread.sleep(15000);
            } catch (Exception e) {
            }
        }
    }
}
