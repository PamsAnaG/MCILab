/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.twitterlab;

import static com.ipn.mci.twitterlab.TwitterConsumer.BYTES_PER_FILE;
import com.ipn.mci.twitterlab.domain.OAuth;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Pam
 */
public class StreamingAPIImpl {

    public void connectStreaming() {

        try {

            OAuth oauth = new OAuth();
            oauth.setConsumerKey("1otukjBXtwAkNkC0m1UwtovIF");
            oauth.setOauthToken("161048591-KxCYXE0gXQUbmnhOLjot4J9LMcTqbrcSS9l2lelo");
            oauth.setConsumerSecret("F2MGdRatio9REWzQR5uisNIPtOW1Mno7S2PV5UeZx3CkabvNoh");
            oauth.setRequestMethod("GET");
            oauth.setRequestURL("https://stream.twitter.com/1.1/statuses/filter.json?language=es&track=MexicovsSuecia");
            oauth.setOauthAccessSecret("p4tqX2mpRppuILzYCmdex5RO08Zug47OtSSTX9raHuFgx");

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oauth.getConsumerKey(), oauth.getConsumerSecret());
            consumer.setTokenWithSecret(oauth.getOauthToken(), oauth.getOauthAccessSecret());
            HttpGet request = new HttpGet(oauth.getRequestURL());
            consumer.sign(request);

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            int iteraciones = 1;
            while (iteraciones <= 100) {
                String line = reader.readLine();
                System.out.println(line);
                iteraciones++;
                if (line == null) {
                    break;
                }
            }

        } catch (Exception excp) {
            excp.printStackTrace();
        }

    }

    public static void main(String arg[]) {

        StreamingAPIImpl servicio = new StreamingAPIImpl();
        servicio.connectStreaming();
    }

}
