/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.domain;

/**
 *
 * @author Pam
 */
public class Tweet {   

    /**
     * @return the idTweet
     */
    public int getIdTweet() {
        return idTweet;
    }

    /**
     * @param idTweet the idTweet to set
     */
    public void setIdTweet(int idTweet) {
        this.idTweet = idTweet;
    }

    /**
     * @return the tweet
     */
    public String getTweet() {
        return tweet;
    }

    /**
     * @param tweet the tweet to set
     */
    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
    private int idTweet;
    private String tweet;
}
