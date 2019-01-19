/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.domain;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Pam
 */
public class ConteoAnalisis {

    private int idConteoDia;
    private int lugar;
    private TipoConteo tipoConteo;
    private Date fhRegistro;
    private String item;
    private int conteoItem;
    private List<UsuarioTw> listaUsuarios;

    /**
     * @return the tipoConteo
     */
    public TipoConteo getTipoConteo() {
        return tipoConteo;
    }

    /**
     * @param tipoConteo the tipoConteo to set
     */
    public void setTipoConteo(TipoConteo tipoConteo) {
        this.tipoConteo = tipoConteo;
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
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * @return the conteoItem
     */
    public int getConteoItem() {
        return conteoItem;
    }

    /**
     * @param conteoItem the conteoItem to set
     */
    public void setConteoItem(int conteoItem) {
        this.conteoItem = conteoItem;
    }

    /**
     * @return the listaUsuarios
     */
    public List<UsuarioTw> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * @param listaUsuarios the listaUsuarios to set
     */
    public void setListaUsuarios(List<UsuarioTw> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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
     * @return the lugar
     */
    public int getLugar() {
        return lugar;
    }

    /**
     * @param lugar the lugar to set
     */
    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

}
