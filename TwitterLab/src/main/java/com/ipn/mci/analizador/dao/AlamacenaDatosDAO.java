/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.dao;

import com.infraestructura.comun.bd.Conexion4;
import com.ipn.mci.analizador.domain.ConteoAnalisis;
import com.ipn.mci.analizador.domain.ResultadosAnalisis;
import com.ipn.mci.analizador.domain.TipoConteo;
import com.ipn.mci.analizador.domain.UsuarioTw;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Pam
 */
public class AlamacenaDatosDAO extends Conexion4 {

    public int insertaResultados(ResultadosAnalisis resultados) throws SQLException {

        String query = "INSERT INTO ANALISIS_DIA (FH_ANALISIS, FH_REGISTRO, TWEETS_ANALIZADOS, PALABRAS_PROMEDIO_TWEETS, PALABRAS, PALABRAS_UNICAS, "
                + "PALABRAS_LIMPIAS, PALABRAS_LIMPIAS_UNICAS, DIVERSIDAD_LEXICA) VALUES\n"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pSt.setDate(1, new java.sql.Date(resultados.getFhAnalisis().getTime()));
        pSt.setDate(2, new java.sql.Date(resultados.getFhRegistro().getTime()));
        pSt.setLong(3, resultados.getTweetsAnalizados());
        pSt.setFloat(4, resultados.getPromedioPalabraTweet());
        pSt.setLong(5, resultados.getNumeroPalabras());
        pSt.setLong(6, resultados.getNumeroPalabrasUnicas());
        pSt.setLong(7, resultados.getNumeroPalabrasLimpias());
        pSt.setLong(8, resultados.getNumeroPalabrasLimpiasUnicas());
        pSt.setDouble(9, resultados.getDivLexica());

        pSt.executeUpdate();

        ResultSet rs = pSt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;

    }

    public int insertaConteo(int idAnalisis, ConteoAnalisis conteo) throws SQLException {

        String query = "INSERT INTO CONTEO_DIA (ID_ANALISIS_DIA, FH_REGISTRO, LUGAR, TIPO, ITEM, CONTEO_ITEM) VALUES\n"
                + "(?, ?, ?, ?, ?, ?)";

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pSt.setInt(1, idAnalisis);
        pSt.setDate(2, new java.sql.Date(conteo.getFhRegistro().getTime()));
        pSt.setInt(3, conteo.getLugar());
        pSt.setInt(4, conteo.getTipoConteo().ordinal());
        pSt.setString(5, conteo.getItem());
        pSt.setInt(6, conteo.getConteoItem());

        pSt.executeUpdate();

        ResultSet rs = pSt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;

    }

    public void insertaConteoUsuario(UsuarioTw usuarioTw, int idTweet, ConteoAnalisis conteo) throws SQLException {

        String query = "INSERT INTO USUARIOS_CONTEO (ID_USUARIO_TW, ID_TWEET, ID_CONTEO_DIA) VALUES\n"
                + "(?, ?, ?)";        

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query);
        pSt.setInt(1, usuarioTw.getIdUsuarioTw());
        pSt.setInt(2, idTweet);
        pSt.setInt(3, conteo.getIdConteoDia());

        pSt.executeUpdate();

    }
    
    public int insertaMencionadaEn(String tweetTexto) throws SQLException {

        String query = "INSERT INTO TWEETS_POPULAR_WRDS (TWEET) VALUES\n"
                + "(?)";

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pSt.setString(1, tweetTexto);
        
        pSt.executeUpdate();

        ResultSet rs = pSt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;

    }

    public int insertaUsuario(UsuarioTw usuarioTw) throws SQLException {

        String query = "INSERT INTO USUARIOS_TW (SCREEN_NAME, NAME, USER_ID, FOLLOWERS, TWEETS, FRIENDS, "
                + "LISTS, TFF_RATIO, INFO_EX) VALUES\n"
                + "(?, ?, ?, ?, ?, ?, ?, ?, 0)";

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pSt.setString(1, usuarioTw.getScreenName());
        pSt.setString(2, usuarioTw.getName());
        pSt.setString(3, usuarioTw.getUserId());
        pSt.setInt(4, usuarioTw.getFollowers());
        pSt.setInt(5, usuarioTw.getTweets());
        pSt.setInt(6, usuarioTw.getFriends());
        pSt.setInt(7, usuarioTw.getLists());
        pSt.setDouble(8, usuarioTw.getTffRatio());

        pSt.executeUpdate();

        ResultSet rs = pSt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;

    }

}
