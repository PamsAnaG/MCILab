CREATE TABLE ANALISIS_DIA(
    ID_ANALISIS_DIA INT NOT NULL AUTO_INCREMENT,  
    FH_ANALISIS DATETIME NOT NULL,
    FH_REGISTRO DATETIME NOT NULL,
    TWEETS_ANALIZADOS INT NOT NULL,
    PALABRAS_PROMEDIO_TWEETS FLOAT(5,3) NOT NULL,
    PALABRAS INT NOT NULL,
    PALABRAS_UNICAS INT NOT NULL,
    PALABRAS_LIMPIAS INT NOT NULL,
    PALABRAS_LIMPIAS_UNICAS INT NOT NULL,
    DIVERSIDAD_LEXICA FLOAT(5,3) NOT NULL,
    ANALISIS_SENTIMIENTO INT DEFAULT 0,
    PRIMARY KEY (ID_ANALISIS_DIA)
);


/*
TIPO_CONTEO:
	1 - Palabra
	2 - Usuario
	3 - HashTag
*/
CREATE TABLE CONTEO_DIA(
    ID_CONTEO_DIA INT NOT NULL AUTO_INCREMENT, 
    ID_ANALISIS_DIA INT NOT NULL,     
    FH_REGISTRO DATETIME NOT NULL,
    LUGAR INT NOT NULL,
    TIPO INT NOT NULL,
    ITEM VARCHAR(500) NOT NULL,
    CONTEO_ITEM INT NOT NULL,
    PRIMARY KEY (ID_CONTEO_DIA),
    CONSTRAINT FK_ANALISIS FOREIGN KEY (ID_ANALISIS_DIA) REFERENCES ANALISIS_DIA (ID_ANALISIS_DIA)
);

/*
INFOMRACION DE USUARIOS
*/
CREATE TABLE USUARIOS_TW(
    ID_USUARIO_TW INT NOT NULL AUTO_INCREMENT, 
    SCREEN_NAME VARCHAR(50) NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    USER_ID VARCHAR(50) NOT NULL,
    FOLLOWERS INT NOT NULL,
    TWEETS INT NOT NULL,
    FRIENDS INT NOT NULL,
    LISTS INT NOT NULL,
    TFF_RATIO INT NOT NULL,
    INFO_EX INT NOT NULL,
    RETWEETS INT NOT NULL,
    REPLIES INT NOT NULL,
    LIKES INT NOT NULL,  
    PRIMARY KEY (ID_USUARIO_TW)    
);

CREATE UNIQUE INDEX USU_TW_UNQ_IDX ON USUARIOS_TW(SCREEN_NAME);

/*
INFOMRACION DE TWEETS ANALIZADOS
*/
CREATE TABLE TWEETS_POPULAR_WRDS(
    ID_TWEET INT NOT NULL AUTO_INCREMENT, 
    TWEET VARCHAR(4000) NOT NULL,    
    PRIMARY KEY (ID_TWEET)    
);

/*
TABLA PARA INDICAR CUALES SON LOS USUARIOS QUE MENCIONARON LAS PALABRAS EN EL CONTEO
*/
CREATE TABLE USUARIOS_CONTEO(
    ID_USU_CONT INT NOT NULL AUTO_INCREMENT, 
    ID_USUARIO_TW INT NOT NULL,
    ID_TWEET INT NOT NULL,
    ID_CONTEO_DIA INT NOT NULL,    
    PRIMARY KEY (ID_USU_CONT),
    CONSTRAINT FK_USU_CONTEO_U FOREIGN KEY (ID_USUARIO_TW) REFERENCES USUARIOS_TW (ID_USUARIO_TW),
    CONSTRAINT FK_USU_CONTEO_T FOREIGN KEY (ID_TWEET) REFERENCES TWEETS_POPULAR_WRDS (ID_TWEET),
    CONSTRAINT FK_USU_CONTEO_C FOREIGN KEY (ID_CONTEO_DIA) REFERENCES CONTEO_DIA (ID_CONTEO_DIA)  
);

CREATE TABLE ANALISIS_SENTIMIENTO(
    ID_ANA_SENT INT NOT NULL AUTO_INCREMENT, 
    ID_CONTEO_DIA INT NOT NULL,
    RATING_GENERAL INT NOT NULL,
    CONTEO_NEGATIVO INT NOT NULL,
    CONTEO_POSITIVO INT NOT NULL,
    RATING_GENERALF INT NOT NULL,
    CONTEO_NEGATIVOF INT NOT NULL,
    CONTEO_POSITIVOF INT NOT NULL,
    PRIMARY KEY (ID_ANA_SENT),      
    CONSTRAINT FK_USU_CONTEO_AS FOREIGN KEY (ID_CONTEO_DIA) REFERENCES CONTEO_DIA (ID_CONTEO_DIA)  
);

INSERT INTO ANALISIS_DIA (FH_ANALISIS, FH_REGISTRO, TWEETS_ANALIZADOS, PALABRAS_PROMEDIO_TWEETS, PALABRAS, PALABRAS_UNICAS, PALABRAS_LIMPIAS, PALABRAS_LIMPIAS_UNICAS, DIVERSIDAD_LEXICA) VALUES
(?, ?, ?, ?, ?, ?, ?, ?, ?)

INSERT INTO CONTEO_DIA (ID_ANALISIS_DIA, FH_REGISTRO, TIPO, ITEM, CONTEO_ITEM) VALUES
(?, ?, ?, ?, ?)

delete from usuarios_conteo;
delete from conteo_dia;
delete from analisis_dia;
delete from TWEETS_POPULAR_WRDS;

---------------------------------------------------------------------------------

select * from analisis_dia order by `FH_ANALISIS`

select * from analisis_dia where id_analisis_dia = 273
select * from conteo_dia where id_analisis_dia = 194
select * from usuarios_tw where info_ex = 0
select * from usuarios_tw where screen_name = 'DrFerNunez'
select * from TWEETS_POPULAR_WRDS;
select * from usuarios_conteo;
select * from ANALISIS_SENTIMIENTO where id_ana_sent = 23 

select * from analisis_dia where id_analisis_dia = 273

delete from ANALISIS_SENTIMIENTO where id_ana_sent = 212

select sum(`TWEETS_ANALIZADOS`), sum(`PALABRAS_PROMEDIO_TWEETS`)/count(*), 
sum(`PALABRAS`), sum(`PALABRAS_UNICAS`), sum(`PALABRAS_LIMPIAS`),
sum(`PALABRAS_LIMPIAS_UNICAS`), sum(`DIVERSIDAD_LEXICA`)/count(*)
from analisis_dia


delete from usuarios_conteo where id_conteo_dia in (select id_conteo_dia from conteo_dia where id_analisis_dia < 211)
delete from  conteo_dia where id_analisis_dia < 211
delete from  analisis_dia where id_analisis_dia < 211


update analisis_dia set analisis_sentimiento = 0 
update usuarios_tw set info_ex = 0 where id_usuario_tw >=162777

select ana.fh_analisis, count(*)
from analisis_dia ana
join conteo_dia con on (ana.ID_ANALISIS_DIA = con.ID_ANALISIS_DIA)
group by ana.fh_analisis


select ana.`ID_ANALISIS_DIA`, ana.`FH_ANALISIS`, con.lugar, 
case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, con.`ITEM`, con.`CONTEO_ITEM`
from analisis_dia ana
join conteo_dia con on (ana.ID_ANALISIS_DIA = con.ID_ANALISIS_DIA)
order by ana.`FH_ANALISIS`, con.TIPO, con.lugar, con.TIPO

select con.`id_conteo_DIA`, con.`ITEM`, dia.fh_analisis, con.LUGAR,  
case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, 
con.`CONTEO_ITEM`, usu.`SCREEN_NAME`, tw.tweet, usu.`FOLLOWERS`, usu.`FRIENDS`, usu.`TWEETS`, usu.`TFF_RATIO`, 
usu.RETWEETS, usu.LIKES, usu.LISTS
from USUARIOS_CONTEO usucon
join CONTEO_DIA con on (usucon.`ID_CONTEO_DIA` = con.`ID_CONTEO_DIA`)
join USUARIOS_TW usu on (usucon.`ID_USUARIO_TW` = usu.`ID_USUARIO_TW`)
join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)
join TWEETS_POPULAR_WRDS tw on (tw.ID_TWEET = usucon.ID_TWEET)
where dia.id_analisis_dia = 280
and con.TIPO = 0
and con.`ITEM` = 'VIDA'
order by con.`id_conteo_DIA`, dia.fh_analisis, con.TIPO DESC


select sum(usu.`TFF_RATIO`), sum(usu.`FOLLOWERS`), sum(usu.LIKES), 
sum(usu.RETWEETS)
from USUARIOS_CONTEO usucon
join CONTEO_DIA con on (usucon.`ID_CONTEO_DIA` = con.`ID_CONTEO_DIA`)
join USUARIOS_TW usu on (usucon.`ID_USUARIO_TW` = usu.`ID_USUARIO_TW`)
join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)
join TWEETS_POPULAR_WRDS tw on (tw.ID_TWEET = usucon.ID_TWEET)
where dia.id_analisis_dia = 280
and con.TIPO = 0
and con.`ITEM` = 'DIAS'
order by con.`id_conteo_DIA`, dia.fh_analisis, con.TIPO DESC


select ana.id_ana_sent, dia.fh_analisis, con.lugar, case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, con.`ITEM`, con.`CONTEO_ITEM`, 
ana.rating_general, ana.rating_generalf, ana.conteo_negativo, ana.conteo_negativoF, ana.conteo_positivo, ana.conteo_positivof
from ANALISIS_SENTIMIENTO ana
join CONTEO_DIA con on (con.`ID_CONTEO_DIA` = ana.`ID_CONTEO_DIA`)
join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)
order by dia.fh_analisis, con.TIPO, con.lugar

select ana.id_ana_sent, dia.fh_analisis, con.lugar, case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, con.`ITEM`, con.`CONTEO_ITEM`, 
case when ana.rating_general <= 0 then 'NEGATIVO' when ana.rating_general > 0 then 'POSITIVO' END RATING_GENERAL, 
case when ana.rating_generalf <= 0 then 'NEGATIVO' when ana.rating_generalf > 0 then 'POSITIVO' END RATING_GENERAL_F, 
ana.conteo_negativo, 
ana.conteo_negativoF, ana.conteo_positivo, ana.conteo_positivof
from ANALISIS_SENTIMIENTO ana
join CONTEO_DIA con on (con.`ID_CONTEO_DIA` = ana.`ID_CONTEO_DIA`)
join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)
order by dia.fh_analisis, con.TIPO, con.lugar


select con.`ITEM`, dia.fh_analisis, 
case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, 
con.`CONTEO_ITEM`, usu.`SCREEN_NAME`, tw.tweet, usu.`FOLLOWERS`, usu.`FRIENDS`, usu.`TWEETS`, usu.`TFF_RATIO`, 
usu.RETWEETS, usu.LIKES
from USUARIOS_CONTEO usucon
join CONTEO_DIA con on (usucon.`ID_CONTEO_DIA` = con.`ID_CONTEO_DIA`)
join USUARIOS_TW usu on (usucon.`ID_USUARIO_TW` = usu.`ID_USUARIO_TW`)
join analisis_dia dia on (dia.id_analisis_dia = con.id_analisis_dia)
join TWEETS_POPULAR_WRDS tw on (tw.ID_TWEET = usucon.ID_TWEET)
order by con.`id_conteo_DIA`, dia.fh_analisis, con.TIPO DESC


