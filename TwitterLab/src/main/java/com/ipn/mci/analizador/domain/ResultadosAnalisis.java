/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Pam
 */
public class ResultadosAnalisis {
    
    private int idResultadosAnalisis;
    private Date fhAnalisis;
    private Date fhRegistro;
    private long tweetsAnalizados;
    private float promedioPalabraTweet;
    private double divLexica;
    private long numeroPalabras;
    private long numeroPalabrasLimpias;
    private long numeroPalabrasUnicas;
    private long numeroPalabrasLimpiasUnicas;
    private List<ConteoAnalisis> conteoAnalisis = new ArrayList();
    private HashMap<String, UsuarioTw> usuarios = new HashMap();

    /**
     * @return the numeroPalabras
     */
    public long getNumeroPalabras() {
        return numeroPalabras;
    }

    /**
     * @param numeroPalabras the numeroPalabras to set
     */
    public void setNumeroPalabras(long numeroPalabras) {
        this.numeroPalabras = numeroPalabras;
    }

    /**
     * @return the numeroPalabrasLimpias
     */
    public long getNumeroPalabrasLimpias() {
        return numeroPalabrasLimpias;
    }

    /**
     * @param numeroPalabrasLimpias the numeroPalabrasLimpias to set
     */
    public void setNumeroPalabrasLimpias(long numeroPalabrasLimpias) {
        this.numeroPalabrasLimpias = numeroPalabrasLimpias;
    }

    /**
     * @return the numeroPalabrasUnicas
     */
    public long getNumeroPalabrasUnicas() {
        return numeroPalabrasUnicas;
    }

    /**
     * @param numeroPalabrasUnicas the numeroPalabrasUnicas to set
     */
    public void setNumeroPalabrasUnicas(long numeroPalabrasUnicas) {
        this.numeroPalabrasUnicas = numeroPalabrasUnicas;
    }

    /**
     * @return the numeroPalabrasLimpiasUnicas
     */
    public long getNumeroPalabrasLimpiasUnicas() {
        return numeroPalabrasLimpiasUnicas;
    }

    /**
     * @param numeroPalabrasLimpiasUnicas the numeroPalabrasLimpiasUnicas to set
     */
    public void setNumeroPalabrasLimpiasUnicas(long numeroPalabrasLimpiasUnicas) {
        this.numeroPalabrasLimpiasUnicas = numeroPalabrasLimpiasUnicas;
    }

    /**
     * @return the conteoAnalisis
     */
    public List<ConteoAnalisis> getConteoAnalisis() {
        return conteoAnalisis;
    }

    /**
     * @param conteoAnalisis the conteoAnalisis to set
     */
    public void setConteoAnalisis(List<ConteoAnalisis> conteoAnalisis) {
        this.conteoAnalisis = conteoAnalisis;
    }    

    /**
     * @return the fhAnalisis
     */
    public Date getFhAnalisis() {
        return fhAnalisis;
    }

    /**
     * @param fhAnalisis the fhAnalisis to set
     */
    public void setFhAnalisis(Date fhAnalisis) {
        this.fhAnalisis = fhAnalisis;
    }

    /**
     * @return the fhRegistro
     */
    public Date getFhRegistro() {
        return fhRegistro;
    }

    /**
     * @param fhRegistro the fhRegistro to set
     */
    public void setFhRegistro(Date fhRegistro) {
        this.fhRegistro = fhRegistro;
    }

    /**
     * @return the tweetsAnalizados
     */
    public long getTweetsAnalizados() {
        return tweetsAnalizados;
    }

    /**
     * @param tweetsAnalizados the tweetsAnalizados to set
     */
    public void setTweetsAnalizados(int tweetsAnalizados) {
        this.tweetsAnalizados = tweetsAnalizados;
    }

    /**
     * @return the promedioPalabraTweet
     */
    public float getPromedioPalabraTweet() {
        return promedioPalabraTweet;
    }

    /**
     * @param promedioPalabraTweet the promedioPalabraTweet to set
     */
    public void setPromedioPalabraTweet(float promedioPalabraTweet) {
        this.promedioPalabraTweet = promedioPalabraTweet;
    }

    /**
     * @return the idResultadosAnalisis
     */
    public int getIdResultadosAnalisis() {
        return idResultadosAnalisis;
    }

    /**
     * @param idResultadosAnalisis the idResultadosAnalisis to set
     */
    public void setIdResultadosAnalisis(int idResultadosAnalisis) {
        this.idResultadosAnalisis = idResultadosAnalisis;
    }

    /**
     * @return the divLexica
     */
    public double getDivLexica() {
        return divLexica;
    }

    /**
     * @param divLexica the divLexica to set
     */
    public void setDivLexica(double divLexica) {
        this.divLexica = divLexica;
    }

    /**
     * @return the usuarios
     */
    public HashMap<String, UsuarioTw> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(HashMap<String, UsuarioTw> usuarios) {
        this.usuarios = usuarios;
    }

}
