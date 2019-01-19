/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.dao;

import com.infraestructura.comun.bd.Conexion4;
import com.infraestructura.comun.bd.Registro;
import com.ipn.mci.analizador.domain.UsuarioTw;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Pam
 */
public class CargaDatosDAO extends Conexion4 {

    public LinkedList<UsuarioTw> getUsuarios() throws SQLException {
        LinkedList<UsuarioTw> usuariosR = new LinkedList();

        sSql = "SELECT * FROM USUARIOS_TW ORDER BY ID_USUARIO_TW";

        ejecutaSql();

        LinkedList<Registro> usuarios = getConsultaLista();
        Iterator usuariosIt = usuarios.iterator();
        while (usuariosIt.hasNext()) {
            Registro registro = (Registro) usuariosIt.next();
            UsuarioTw usuario = new UsuarioTw();
            usuario.setFollowers(Integer.valueOf((String) registro.getDefCampo("FOLLOWERS")));
            usuario.setFriends(Integer.valueOf((String) registro.getDefCampo("FRIENDS")));
            usuario.setLists(Integer.valueOf((String) registro.getDefCampo("LISTS")));
            usuario.setName((String) registro.getDefCampo("NAME"));
            usuario.setScreenName((String) registro.getDefCampo("SCREEN_NAME"));
            usuario.setTweets(Integer.valueOf((String) registro.getDefCampo("TWEETS")));
            usuario.setUserId((String) registro.getDefCampo("TWEETS"));
            usuario.setTffRatio(Integer.valueOf((String) registro.getDefCampo("TWEETS")));
            usuario.setPreCargado(1);
            usuario.setIdUsuarioTw(Integer.valueOf((String) registro.getDefCampo("ID_USUARIO_TW")));

            usuariosR.add(usuario);
        }

        return usuariosR;

    }

    public LinkedList getUsuariosNoInfo() throws SQLException {
        LinkedList<UsuarioTw> usuariosR = new LinkedList();

        sSql = "SELECT * FROM USUARIOS_TW WHERE INFO_EX = 0 ORDER BY ID_USUARIO_TW";

        ejecutaSql();

        LinkedList<Registro> usuarios = getConsultaLista();
        Iterator usuariosIt = usuarios.iterator();
        while (usuariosIt.hasNext()) {
            Registro registro = (Registro) usuariosIt.next();
            UsuarioTw usuario = new UsuarioTw();
            usuario.setFollowers(Integer.valueOf((String) registro.getDefCampo("FOLLOWERS")));
            usuario.setFriends(Integer.valueOf((String) registro.getDefCampo("FRIENDS")));
            usuario.setLists(Integer.valueOf((String) registro.getDefCampo("LISTS")));
            usuario.setName((String) registro.getDefCampo("NAME"));
            usuario.setScreenName((String) registro.getDefCampo("SCREEN_NAME"));
            usuario.setTweets(Integer.valueOf((String) registro.getDefCampo("TWEETS")));
            usuario.setUserId((String) registro.getDefCampo("TWEETS"));
            usuario.setTffRatio(Integer.valueOf((String) registro.getDefCampo("TWEETS")));
            usuario.setPreCargado(1);
            usuario.setIdUsuarioTw(Integer.valueOf((String) registro.getDefCampo("ID_USUARIO_TW")));

            usuariosR.add(usuario);
        }

        return usuariosR;

    }

    public void actualizaUsuarioInfo(int idUsuarioTw) throws SQLException {

        sSql = "UPDATE USUARIOS_TW SET INFO_EX = 1 WHERE ID_USUARIO_TW = " + idUsuarioTw;

        ejecutaUpdate();

    }

}
