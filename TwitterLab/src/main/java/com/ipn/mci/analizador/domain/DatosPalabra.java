/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Pam
 */
public class DatosPalabra {
    
    private List<UsuarioTw> mencionadaPor = new ArrayList();
    private List<Tweet> mencionadaEn = new ArrayList();

    /**
     * @return the mencionadaPor
     */
    public List<UsuarioTw> getMencionadaPor() {
        return mencionadaPor;
    }

    /**
     * @param mencionadaPor the mencionadaPor to set
     */
    public void setMencionadaPor(List<UsuarioTw> mencionadaPor) {
        this.mencionadaPor = mencionadaPor;
    }

    /**
     * @return the mencionadaEn
     */
    public List<Tweet> getMencionadaEn() {
        return mencionadaEn;
    }

    /**
     * @param mencionadaEn the mencionadaEn to set
     */
    public void setMencionadaEn(List<Tweet> mencionadaEn) {
        this.mencionadaEn = mencionadaEn;
    }
    
}
