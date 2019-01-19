/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ipn.mci.analizador.domain;

/**
 *
 * @author pamela.gutierrez
 */
public class UsuarioTw {
    
    private int idUsuarioTw;
    private String screenName;
    private String name;
    private String userId;
    private int followers;
    private int tweets;
    private int friends;
    private int lists;
    private double tffRatio;
    private int preCargado = 0;
    

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the followers
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * @param followers the followers to set
     */
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    /**
     * @return the tweets
     */
    public int getTweets() {
        return tweets;
    }

    /**
     * @param tweets the tweets to set
     */
    public void setTweets(int tweets) {
        this.tweets = tweets;
    }

    /**
     * @return the friends
     */
    public int getFriends() {
        return friends;
    }

    /**
     * @param friends the friends to set
     */
    public void setFriends(int friends) {
        this.friends = friends;
    }

    /**
     * @return the lists
     */
    public int getLists() {
        return lists;
    }

    /**
     * @param lists the lists to set
     */
    public void setLists(int lists) {
        this.lists = lists;
    }

    /**
     * @return the tffRatio
     */
    public double getTffRatio() {
        return tffRatio;
    }

    /**
     * @param tffRatio the tffRatio to set
     */
    public void setTffRatio(double tffRatio) {
        this.tffRatio = tffRatio;
    }

    /**
     * @return the idUsuarioTw
     */
    public int getIdUsuarioTw() {
        return idUsuarioTw;
    }

    /**
     * @param idUsuarioTw the idUsuarioTw to set
     */
    public void setIdUsuarioTw(int idUsuarioTw) {
        this.idUsuarioTw = idUsuarioTw;
    }

    /**
     * @return the preCargado
     */
    public int getPreCargado() {
        return preCargado;
    }

    /**
     * @param preCargado the preCargado to set
     */
    public void setPreCargado(int preCargado) {
        this.preCargado = preCargado;
    }
    
}
