/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.twitterlab;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Pam
 */
public class UserTimelineConsumer {

    private String accessToken = "";
    private String accessSecret = "";
    private String consumerKey = "";
    private String consumerSecret = "";
    private String userTimelineUrl;
    private String screenName;

    public static void main(String[] args) {
        UserTimelineConsumer consumer = new UserTimelineConsumer(
                "161048591-KxCYXE0gXQUbmnhOLjot4J9LMcTqbrcSS9l2lelo",
                "p4tqX2mpRppuILzYCmdex5RO08Zug47OtSSTX9raHuFgx",
                "1otukjBXtwAkNkC0m1UwtovIF",
                "F2MGdRatio9REWzQR5uisNIPtOW1Mno7S2PV5UeZx3CkabvNoh",
                "https://api.twitter.com/1.1/statuses/user_timeline.json", "pamaid23");

        consumer.consumeTimeLine();
    }

    public UserTimelineConsumer(String accessToken, String accessSecret, String consumerKey, String consumerSecret, String url, String screenName) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        userTimelineUrl = url;
        this.screenName = screenName;
    }

    public void setScreename(String screenName) {
        this.screenName = screenName;
    }

    public String consumeTimeLine() {

        try {

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
            consumer.setTokenWithSecret(accessToken, accessSecret);
            String url = userTimelineUrl + "?screen_name=" + screenName + "&count=50&trim_user=true&exclude_replies=true";

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            consumer.sign(request);

            //request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);

            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        return "";

    }

}
