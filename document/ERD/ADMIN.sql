SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS CMN_AUTHORINFO;
DROP TABLE IF EXISTS CMN_MENU_INFO;
DROP TABLE IF EXISTS CMN_ROLE_INFO;




/* Create Tables */

CREATE TABLE CMN_AUTHORINFO
(
	AUTH_ID varchar(100) NOT NULL,
	AUTH_NM varchar(200),
	DESCRIPTION varchar(2000),
	ROLE_INFO varchar(2000),
	CREATE_ID varchar(100),
	CREATE_DT date,
	UPDATE_ID varchar(100),
	UPDATE_DT date,
	PRIMARY KEY (AUTH_ID)
);


CREATE TABLE CMN_MENU_INFO
(
	MENU_ID varchar(100) NOT NULL,
	MENU_NM varchar(200),
	UP_MENU_ID varchar(50),
	MENU_URL varchar(500),
	MENU_TYPE varchar(200),
	CREATE_ID varchar(100),
	CREATE_DT date,
	UPDATE_ID varchar(100),
	UPDATE_DT date,
	PRIMARY KEY (MENU_ID)
);


CREATE TABLE CMN_ROLE_INFO
(
	ROLE_ID varchar(100) NOT NULL,
	ROLE_NM varchar(200),
	ROLE_TYPE varchar(100),
	ROLE_SORT int,
	DESCRIPTION varchar(2000),
	ROLE_MENU varchar(2000),
	CREATE_ID varchar(100),
	CREATE_DT date,
	UPDATE_ID varchar(100),
	UPDATE_DT date,
	PRIMARY KEY (ROLE_ID)
);



