/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.analizador.dao;

import com.infraestructura.comun.bd.Conexion4;
import com.infraestructura.comun.bd.Registro;
import com.ipn.mci.analizador.domain.AnalisisSentimiento;
import com.ipn.mci.analizador.domain.ConteoAnalisis;
import com.ipn.mci.analizador.domain.ResultadosAnalisis;
import com.ipn.mci.analizador.domain.UsuarioTw;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Pam
 */
public class FiltraResultadosDAO extends Conexion4 {

    public LinkedList<ResultadosAnalisis> getDiasAnalisis() throws SQLException {
        LinkedList<ResultadosAnalisis> fechas = new LinkedList();

        sSql = "SELECT ID_ANALISIS_DIA, FH_ANALISIS, FH_REGISTRO\n"
                + "FROM ANALISIS_DIA \n"
                + "WHERE ANALISIS_SENTIMIENTO = 0";

        ejecutaSql();

        LinkedList<Registro> usuarios = getConsultaLista();
        Iterator usuariosIt = usuarios.iterator();
        while (usuariosIt.hasNext()) {
            Registro registro = (Registro) usuariosIt.next();
            ResultadosAnalisis resultados = new ResultadosAnalisis();
            resultados.setFhAnalisis((Date) registro.getDefCampo("FH_ANALISIS"));
            resultados.setIdResultadosAnalisis(Integer.valueOf((String) registro.getDefCampo("ID_ANALISIS_DIA")));
            fechas.add(resultados);
        }

        return fechas;
    }

    public LinkedList<ConteoAnalisis> getRegistrosDias(int idAnalisisDia, int tipoRegistro) throws SQLException {

        LinkedList<ConteoAnalisis> conteoR = new LinkedList();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        sSql = "select con.`id_conteo_DIA`, con.`ITEM`, dia.fh_analisis, con.LUGAR,  \n"
                + "case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, \n"
                + "con.`CONTEO_ITEM`, usu.`SCREEN_NAME`, tw.tweet, usu.`FOLLOWERS`, usu.`FRIENDS`, usu.`TWEETS`, usu.`TFF_RATIO`, \n"
                + "usu.RETWEETS, usu.LIKES, usu.LISTS\n"
                + "from USUARIOS_CONTEO usucon\n"
                + "join CONTEO_DIA con on (usucon.`ID_CONTEO_DIA` = con.`ID_CONTEO_DIA`)\n"
                + "join USUARIOS_TW usu on (usucon.`ID_USUARIO_TW` = usu.`ID_USUARIO_TW`)\n"
                + "join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)\n"
                + "join TWEETS_POPULAR_WRDS tw on (tw.ID_TWEET = usucon.ID_TWEET)\n"
                + "where dia.id_analisis_dia = " + idAnalisisDia + "\n"
                + "and con.TIPO = " + tipoRegistro + "\n"
                + "order by con.`id_conteo_DIA`, dia.fh_analisis, con.TIPO DESC";

        ejecutaSql();

        LinkedList<Registro> conteoAnalisis = getConsultaLista();
        Iterator conteoAnalisisIt = conteoAnalisis.iterator();
        ConteoAnalisis conteo = new ConteoAnalisis();
        int conteoItem = 0;
        while (conteoAnalisisIt.hasNext()) {
            Registro registro = (Registro) conteoAnalisisIt.next();

            if (conteoItem != 0 && Integer.valueOf((String) registro.getDefCampo("ID_CONTEO_DIA")) != conteoItem) {
                conteoR.add(conteo);
                conteoItem = 0;
            }

            if (conteoItem == 0) {
                conteoItem = Integer.valueOf((String) registro.getDefCampo("ID_CONTEO_DIA"));
                conteo = new ConteoAnalisis();
                conteo.setConteoItem(Integer.valueOf((String) registro.getDefCampo("CONTEO_ITEM")));
                conteo.setIdConteoDia(Integer.valueOf((String) registro.getDefCampo("ID_CONTEO_DIA")));
                conteo.setItem((String) registro.getDefCampo("ITEM"));
                conteo.setLugar(Integer.valueOf((String) registro.getDefCampo("LUGAR")));
            }

            UsuarioTw usuario = new UsuarioTw();
            usuario.setFollowers(Integer.valueOf((String) registro.getDefCampo("FOLLOWERS")));
            usuario.setFriends(Integer.valueOf((String) registro.getDefCampo("FRIENDS")));
            usuario.setLikes(Integer.valueOf((String) registro.getDefCampo("LIKES")));
            usuario.setLists(Integer.valueOf((String) registro.getDefCampo("LISTS")));
            usuario.setScreenName((String) registro.getDefCampo("SCREEN_NAME"));
            usuario.setReTweets(Integer.valueOf((String) registro.getDefCampo("RETWEETS")));
            usuario.setTffRatio(Integer.valueOf((String) registro.getDefCampo("TFF_RATIO")));
            usuario.setTweetTexto((String) registro.getDefCampo("TWEET"));
            usuario.setTweets(Integer.valueOf((String) registro.getDefCampo("TWEETS")));
            conteo.getListaUsuarios().add(usuario);

        }
        conteoR.add(conteo);
        return conteoR;

    }

    public void insertaResultadoFinal(AnalisisSentimiento analisis) throws SQLException {

        String query = "INSERT INTO ANALISIS_SENTIMIENTO (ID_CONTEO_DIA, RATING_GENERAL, CONTEO_NEGATIVO, CONTEO_POSITIVO, RATING_GENERALF, "
                + "CONTEO_NEGATIVOF, CONTEO_POSITIVOF) VALUES\n"
                + "(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pSt;
        pSt = this.conn.prepareStatement(query);
        pSt.setInt(1, analisis.getIdConteoDia());
        pSt.setInt(2, analisis.getRatingGeneral());
        pSt.setInt(3, analisis.getConteoNegativo());
        pSt.setInt(4, analisis.getConteoPositivo());
        pSt.setInt(5, analisis.getRatingGeneralF());
        pSt.setInt(6, analisis.getConteoNegativoF());
        pSt.setInt(7, analisis.getConteoPositivoF());

        pSt.executeUpdate();

    }

    public void actualizaUsuarioInfo(int idAnalisisDia) throws SQLException {

        sSql = "UPDATE analisis_dia SET ANALISIS_SENTIMIENTO = 1 WHERE ID_ANALISIS_DIA = " + idAnalisisDia;

        ejecutaUpdate();

    }

}
