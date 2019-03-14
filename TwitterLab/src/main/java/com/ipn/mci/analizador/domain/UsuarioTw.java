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

    /**
     * @return the tweetTexto
     */
    public String getTweetTexto() {
        return tweetTexto;
    }

    /**
     * @param tweetTexto the tweetTexto to set
     */
    public void setTweetTexto(String tweetTexto) {
        this.tweetTexto = tweetTexto;
    }

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
    private int infoEx = 0;
    private int reTweets = 0;
    private int replies = 0;
    private int likes = 0;
    // PROPIEDAD UTILIZADA UNICAMENTE PARA LA RELACION INICIAL
    private String tweetTexto;

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

    /**
     * @return the infoEx
     */
    public int getInfoEx() {
        return infoEx;
    }

    /**
     * @param infoEx the infoEx to set
     */
    public void setInfoEx(int infoEx) {
        this.infoEx = infoEx;
    }

    /**
     * @return the reTweets
     */
    public int getReTweets() {
        return reTweets;
    }

    /**
     * @param reTweets the reTweets to set
     */
    public void setReTweets(int reTweets) {
        this.reTweets = reTweets;
    }

    /**
     * @return the replies
     */
    public int getReplies() {
        return replies;
    }

    /**
     * @param replies the replies to set
     */
    public void setReplies(int replies) {
        this.replies = replies;
    }

    /**
     * @return the likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * @param likes the likes to set
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

}
