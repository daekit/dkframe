
/* Drop Tables */

DROP TABLE BM_METERIAL_CODE;




/* Create Tables */

CREATE TABLE BM_METERIAL_CODE
(
	METERIAL_CD varchar(100) NOT NULL,
	STOCK integer,
	B_CD varchar(100),
	ACCOUNT_CD varchar(100),
	S_ACCOUNT_CD varchar(100),
	ITEM_NAME varchar(200),
	MATTER_CD varchar(100),
	FEATURE varchar(200),
	TRADE_UNIT varchar(200),
	PRICE_UNIT varchar(200),
	STOCK_UNIT varchar(200),
	UNIT_WEIGHT varchar(200),
	WEIGHT varchar(200),
	PRICE integer,
	SELL_PRICE_A integer,
	-- 
	-- 
	SELL_PRICE_B integer,
	SELL_PRICE_C integer,
	SELL_PRICE_D integer,
	WAREHOUSE_NO varchar(200),
	MAIN_CUSTOMER varchar(200),
	STOCK_STANDARD varchar(200),
	PART_QTY integer,
	PW_CAL varchar(200),
	PRIMARY KEY (METERIAL_CD)
);



