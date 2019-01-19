CREATE TABLE ANALISIS_DIA(
    ID_ANALISIS_DIA INT NOT NULL AUTO_INCREMENT,  
    FH_ANALISIS DATE NOT NULL,
    FH_REGISTRO DATETIME NOT NULL,
    TWEETS_ANALIZADOS INT NOT NULL,
    PALABRAS_PROMEDIO_TWEETS FLOAT(5,3) NOT NULL,
    PALABRAS INT NOT NULL,
    PALABRAS_UNICAS INT NOT NULL,
    PALABRAS_LIMPIAS INT NOT NULL,
    PALABRAS_LIMPIAS_UNICAS INT NOT NULL,
    DIVERSIDAD_LEXICA FLOAT(5,3) NOT NULL,    
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
    PRIMARY KEY (ID_USUARIO_TW)    
);

CREATE UNIQUE INDEX USU_TW_UNQ_IDX ON USUARIOS_TW(SCREEN_NAME);

/*
TABLA PARA INDICAR CUALES SON LOS USUARIOS QUE MENCIONARON LAS PALABRAS EN EL CONTEO
*/
CREATE TABLE USUARIOS_CONTEO(
    ID_USU_CONT INT NOT NULL AUTO_INCREMENT, 
    ID_USUARIO_TW INT NOT NULL,
    ID_CONTEO_DIA INT NOT NULL,    
    PRIMARY KEY (ID_USU_CONT),
    CONSTRAINT FK_USU_CONTEO_U FOREIGN KEY (ID_USUARIO_TW) REFERENCES USUARIOS_TW (ID_USUARIO_TW),
    CONSTRAINT FK_USU_CONTEO_C FOREIGN KEY (ID_CONTEO_DIA) REFERENCES CONTEO_DIA (ID_CONTEO_DIA)  
);

INSERT INTO ANALISIS_DIA (FH_ANALISIS, FH_REGISTRO, TWEETS_ANALIZADOS, PALABRAS_PROMEDIO_TWEETS, PALABRAS, PALABRAS_UNICAS, PALABRAS_LIMPIAS, PALABRAS_LIMPIAS_UNICAS, DIVERSIDAD_LEXICA) VALUES
(?, ?, ?, ?, ?, ?, ?, ?, ?)

INSERT INTO CONTEO_DIA (ID_ANALISIS_DIA, FH_REGISTRO, TIPO, ITEM, CONTEO_ITEM) VALUES
(?, ?, ?, ?, ?)

select * from analisis_dia
select * from conteo_dia

delete from analisis_dia
delete from conteo_dia

select ana.`ID_ANALISIS_DIA`, ana.`FH_ANALISIS`, 
case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, con.`ITEM`, con.`CONTEO_ITEM`
from analisis_dia ana
join conteo_dia con on (ana.ID_ANALISIS_DIA = con.ID_ANALISIS_DIA)
order by con.TIPO, con.`CONTEO_ITEM` desc


select con.`ITEM`, 
case con.TIPO when 0 then 'PALABRA' when 1 then 'USUARIO' when 2 then 'HASHTAG' END TIPO, 
con.`CONTEO_ITEM`, usu.`SCREEN_NAME`, usu.`FOLLOWERS`, usu.`FRIENDS`, usu.`TWEETS`, usu.`TFF_RATIO`
from USUARIOS_CONTEO usucon
join CONTEO_DIA con on (usucon.`ID_CONTEO_DIA` = con.`ID_CONTEO_DIA`)
join USUARIOS_TW usu on (usucon.`ID_USUARIO_TW` = usu.`ID_USUARIO_TW`)