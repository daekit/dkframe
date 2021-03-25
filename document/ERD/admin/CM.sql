
/* Drop Tables */

DROP TABLE TB_CM01M01;
DROP TABLE TB_CM02M01;
DROP TABLE TB_CM03M01;
DROP TABLE TB_CM04M01;
DROP TABLE TB_CM05M01;
DROP TABLE TB_CM06H01;
DROP TABLE TB_CM06H02;
DROP TABLE TB_CM06M01;
DROP TABLE TB_CM07M01;
DROP TABLE TB_CM08M01;
DROP TABLE TB_CM09M01;




/* Create Tables */

CREATE TABLE TB_CM01M01
(
	AUTH_ID varchar(50) NOT NULL,
	AUTH_NM varchar(100),
	EXPL varchar(2000),
	AUTH_ROLE varchar(500),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM01M01_PK PRIMARY KEY (AUTH_ID)
);


CREATE TABLE TB_CM02M01
(
	ROLE_ID varchar(20) NOT NULL,
	ROLE_NM varchar(100),
	ROLE_TYPE varchar(20),
	ROLE_PTTRN varchar(200),
	EXPL varchar(2000),
	ROLE_MENU varchar(500),
	SORT_NO integer,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM02M01_PK PRIMARY KEY (ROLE_ID)
);


CREATE TABLE TB_CM03M01
(
	MENU_ID varchar(20) NOT NULL,
	MENU_NM varchar(100),
	UP_MENU_ID varchar(20),
	MENU_URL varchar(500),
	MENU_TYPE varchar(20),
	SORT_NO integer,
	USE_YN char,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM03M01_PK PRIMARY KEY (MENU_ID)
);


CREATE TABLE TB_CM04M01
(
	DEPT_ID varchar(20) NOT NULL,
	DEPT_NM varchar(100),
	UP_DEPT_ID varchar(20),
	DEPT_TYPE varchar(50),
	SORT_NO integer,
	USE_YN char,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM04M01_PK PRIMARY KEY (DEPT_ID)
);


CREATE TABLE TB_CM05M01
(
	CODE_ID varchar(20) NOT NULL,
	CODE_NM varchar(100),
	CODE_KIND varchar(20),
	CODE_DESC varchar(500),
	SORT_NO integer,
	USE_YN char,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM05M01_PK PRIMARY KEY (CODE_ID)
);


CREATE TABLE TB_CM06H01
(
	ID varchar(50) NOT NULL,
	NAME varchar(100),
	TOT_CNT integer,
	LAST_DT timestamp,
	CONSTRAINT TB_CM06H01_PK PRIMARY KEY (ID)
);


CREATE TABLE TB_CM06H02
(
	HIS_NO integer NOT NULL,
	ID varchar(50) NOT NULL,
	NAME varchar(100),
	PGM_ID varchar(100),
	ACCS_DT timestamp,
	CONSTRAINT TB_CM06H02_PK PRIMARY KEY (HIS_NO)
);


CREATE TABLE TB_CM06M01
(
	ID varchar(50) NOT NULL,
	PASSWORD varchar(200),
	EMP_NO varchar(20),
	NAME varchar(100),
	CO_CD varchar(10),
	DEPT_ID varchar(20),
	LEVEL_CD varchar(20),
	EMAIL varchar(100),
	AUTH_INFO varchar(200),
	ENTER_DT date,
	-- 사용/삭제 여부(Y/N)
	USE_YN char(1),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM06M01_PK PRIMARY KEY (ID)
);


CREATE TABLE TB_CM07M01
(
	LEVEL_CD varchar(20) NOT NULL,
	LEVEL_NM varchar(100),
	USE_YN char,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	CONSTRAINT TB_CM07M01_PK PRIMARY KEY (LEVEL_CD)
);


CREATE TABLE TB_CM08M01
(
	FILE_KEY numeric NOT NULL,
	FILE_SIZE numeric,
	FILE_TYPE varchar(5),
	FILE_NAME varchar(50),
	-- 저장 파일 경로(서버)
	FILE_PATH varchar(255),
	-- 프로젝트(PT)
	-- 주문서(OD)
	-- 거래처(CT)
	FILE_TRGT_TYP varchar(10),
	FILE_TRGT_KEY varchar(20),
	-- 사용/삭제 여부(Y/N)
	USE_YN char(1),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (FILE_KEY)
);


CREATE TABLE TB_CM09M01
(
	NOTI_KEY integer NOT NULL,
	CO_CD varchar(5),
	NOTI_TITLE varchar(255),
	NOTI_CNTS varchar(8000),
	-- Y/N
	EXPRTN_YN char(1),
	EXPRTN_DT varchar(8),
	-- Y/N
	POPUP_YN char(1),
	-- Y/N
	USE_YN char(1),
	-- Y/N
	SAVE_YN char(1),
	READ_CNT integer DEFAULT 0,
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (NOTI_KEY)
);



