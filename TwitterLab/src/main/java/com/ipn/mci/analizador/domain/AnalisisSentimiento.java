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
public class AnalisisSentimiento {
    
    private int idAnalisisSentimiento;
    private int idConteoDia;
    private int ratingGeneral;
    private int conteoNegativo;
    private int conteoPositivo;
    // ANALISIS DE SENTIMIENTOS UTILIZANDO FREELING
    private int ratingGeneralF;
    private int conteoNegativoF;
    private int conteoPositivoF;

    /**
     * @return the idAnalisisSentimiento
     */
    public int getIdAnalisisSentimiento() {
        return idAnalisisSentimiento;
    }

    /**
     * @param idAnalisisSentimiento the idAnalisisSentimiento to set
     */
    public void setIdAnalisisSentimiento(int idAnalisisSentimiento) {
        this.idAnalisisSentimiento = idAnalisisSentimiento;
    }

    /**
     * @return the idConteoDia
     */
    public int getIdConteoDia() {
        return idConteoDia;
    }

    /**
     * @param idConteoDia the idConteoDia to set
     */
    public void setIdConteoDia(int idConteoDia) {
        this.idConteoDia = idConteoDia;
    }

    /**
     * @return the ratingGeneral
     */
    public int getRatingGeneral() {
        return ratingGeneral;
    }

    /**
     * @param ratingGeneral the ratingGeneral to set
     */
    public void setRatingGeneral(int ratingGeneral) {
        this.ratingGeneral = ratingGeneral;
    }

    /**
     * @return the conteoNegativo
     */
    public int getConteoNegativo() {
        return conteoNegativo;
    }

    /**
     * @param conteoNegativo the conteoNegativo to set
     */
    public void setConteoNegativo(int conteoNegativo) {
        this.conteoNegativo = conteoNegativo;
    }

    /**
     * @return the conteoPositivo
     */
    public int getConteoPositivo() {
        return conteoPositivo;
    }

    /**
     * @param conteoPositivo the conteoPositivo to set
     */
    public void setConteoPositivo(int conteoPositivo) {
        this.conteoPositivo = conteoPositivo;
    }       

    /**
     * @return the ratingGeneralF
     */
    public int getRatingGeneralF() {
        return ratingGeneralF;
    }

    /**
     * @param ratingGeneralF the ratingGeneralF to set
     */
    public void setRatingGeneralF(int ratingGeneralF) {
        this.ratingGeneralF = ratingGeneralF;
    }

    /**
     * @return the conteoNegativoF
     */
    public int getConteoNegativoF() {
        return conteoNegativoF;
    }

    /**
     * @param conteoNegativoF the conteoNegativoF to set
     */
    public void setConteoNegativoF(int conteoNegativoF) {
        this.conteoNegativoF = conteoNegativoF;
    }

    /**
     * @return the conteoPositivoF
     */
    public int getConteoPositivoF() {
        return conteoPositivoF;
    }

    /**
     * @param conteoPositivoF the conteoPositivoF to set
     */
    public void setConteoPositivoF(int conteoPositivoF) {
        this.conteoPositivoF = conteoPositivoF;
    }
    
}
