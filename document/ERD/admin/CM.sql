SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS CMN_CODE_INFO;
DROP TABLE IF EXISTS TB_CM01M01;
DROP TABLE IF EXISTS TB_CM02M01;
DROP TABLE IF EXISTS TB_CM03M01;
DROP TABLE IF EXISTS TB_CM08M01;




/* Create Tables */

CREATE TABLE CMN_CODE_INFO
(
	CODE_ID varchar(100) NOT NULL,
	CODE_NM varchar(200),
	CODE_KIND varchar(200),
	CODE_DESC varchar(500),
	USE_YN char,
	INSERT_ID varchar(20),
	INSERT_DT date,
	UPDATE_ID varchar(20),
	UPDATE_DT date,
	PRIMARY KEY (CODE_ID)
);


CREATE TABLE TB_CM01M01
(
	AUTH_ID varchar(50) NOT NULL,
	AUTH_NM varchar(100),
	EXPL varchar(2000),
	AUTH_ROLE varchar(500),
	INSERT_ID varchar(20),
	INSERT_DT date,
	UPDATE_ID varchar(20),
	UPDATE_DT date,
	CONSTRAINT TB_CM01M01_PK PRIMARY KEY (AUTH_ID)
);


CREATE TABLE TB_CM02M01
(
	ROLE_ID varchar(20) NOT NULL,
	ROLE_NM varchar(100),
	ROLE_TYPE varchar(20),
	ROLE_SORT int,
	ROLE_PTTRN varchar(200),
	EXPL varchar(2000),
	ROLE_MENU varchar(500),
	INSERT_ID varchar(20),
	INSERT_DT date,
	UPDATE_ID varchar(20),
	UPDATE_DT date,
	CONSTRAINT TB_CM02M01_PK PRIMARY KEY (ROLE_ID)
);


CREATE TABLE TB_CM03M01
(
	MENU_ID varchar(20) NOT NULL,
	MENU_NM varchar(100),
	UP_MENU_ID varchar(20),
	MENU_URL varchar(500),
	MENU_TYPE varchar(20),
	USE_YN char,
	INSERT_ID varchar(20),
	INSERT_DT date,
	UPDATE_ID varchar(20),
	UPDATE_DT date,
	CONSTRAINT TB_CM03M01_PK PRIMARY KEY (MENU_ID)
);


CREATE TABLE TB_CM08M01
(
	FILE_KEY numeric NOT NULL,
	FILE_SIZE numeric,
	FILE_TYPE varchar(5),
	FILE_NAME varchar(50),
	-- 저장 파일 경로(서버)
	FILE_PATH varchar(255) COMMENT '저장 파일 경로(서버)',
	-- 프로젝트(PT)
	-- 주문서(OD)
	-- 거래처(CT)
	FILE_TRGT_TYP varchar(10) COMMENT '프로젝트(PT)
주문서(OD)
거래처(CT)',
	FILE_TRGT_KEY varchar(20),
	-- 사용/삭제 여부(Y/N)
	USE_YN char(1) COMMENT '사용/삭제 여부(Y/N)',
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UPD_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (FILE_KEY)
);



