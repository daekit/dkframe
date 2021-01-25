
/* Drop Tables */

DROP TABLE BM_CUSTOMER_DIVISION;
DROP TABLE BM_CUSTOMER_SECURITY;
DROP TABLE BM_CUSTOMER_MAST;
DROP TABLE BM_GOODS_CODE;
DROP TABLE BM_MATERIAL_CODE;




/* Create Tables */

CREATE TABLE BM_CUSTOMER_DIVISION
(
	-- 거래처 마스터 자동증가값
	C_ID integer NOT NULL,
	-- 거래처 사업부 순번
	C_DIV_ID integer NOT NULL,
	-- 거래처 사업부 구분 코드
	C_DIV_GUBUN_CD nvarchar(10),
	-- 사업부명
	C_DIV_NM nvarchar(25),
	-- 거래처 사업부 담당자명
	C_DIV_MNGT_NM nvarchar(10),
	-- 거래처 담당자 전화번호
	C_DIV_MNGT_TEL_NO nvarchar(15),
	-- 거래처 담당자 휴대전화번호
	C_DIV_MNGT_HP_NO nvarchar(15),
	-- 거래처 담당자 이메일
	C_DIV_MNGT_EMAIL nvarchar(50),
	-- 거래처 담당자 부서명
	C_DIV_MNGT_ORG_NM nvarchar(20),
	-- 거래처 담당자 직급명
	C_DIV_MNGT_POS_NM nvarchar(15),
	-- 거래쳐 영업 담당 회사코드
	C_DIV_SALE_CMP_CD nvarchar(10),
	-- 거래처 영업 담당 사원 ID
	C_DIV_SALE_MNGT_ID nvarchar(20),
	PRIMARY KEY (C_DIV_ID)
);


CREATE TABLE BM_CUSTOMER_MAST
(
	-- 거래처 마스터 자동증가값
	C_ID integer NOT NULL,
	-- 매입/매출/운송 기타
	C_GUBUN_CD nvarchar(5),
	-- 거래처명
	C_COMPANY_NM nvarchar(50),
	-- 사업자 등록 번호
	C_CORPOR_REG_NO nvarchar(12),
	-- 대표자명
	C_REP_NM nvarchar(20),
	-- 업태
	C_TYPE nvarchar(20),
	-- 종목
	C_ITEM nvarchar(25),
	C_ADDR_POST nvarchar(7),
	-- 사업자 주소
	C_ADDR_MAIN nvarchar(50),
	-- 사업자 주소 상세
	C_ADDR_DTL nvarchar(50),
	-- 전화번호
	C_TEL_NO nvarchar(13),
	-- 사업자 FAX 번호
	C_FAX_NO nvarchar(13),
	-- 보조 메모 URL
	C_URL nvarchar(40),
	-- 은행 코드
	C_BANK_CD nvarchar(10),
	-- 은행 계좌 번호
	C_BANK_NO nvarchar(20),
	-- 거래처 메모
	C_CONT nvarchar(255),
	-- 여신 금액
	C_LOAN_AMOUNT integer,
	PRIMARY KEY (C_ID)
);


CREATE TABLE BM_CUSTOMER_SECURITY
(
	-- 거래처 마스터 자동증가값
	C_ID integer NOT NULL,
	-- 거래처 담보 내역 순번
	C_SEC_ID integer NOT NULL,
	-- 거래처 담보 구분
	C_SEC_GUBUN nvarchar(10),
	C_SEC_CMP_ID nvarchar(10),
	-- 거래처 담보 금액
	C_SEC_AMOUNT integer,
	-- 거래처 담보 인정 비율
	C_SEC_LOAN_RAT integer,
	-- 거래처 담보 내역 비고
	C_SEC_CONT nvarchar(255),
	PRIMARY KEY (C_SEC_ID)
);


CREATE TABLE BM_GOODS_CODE
(
	GOODS_CD varchar(100) NOT NULL,
	STOCK_YN char,
	B_CD varchar(100),
	M_CD varchar(100),
	GOODS_NM varchar(200),
	REAL_GOODS_NM varchar(200),
	SLT_NM varchar(200),
	SHEET_NM varchar(200),
	BASS_LT varchar(200),
	QTY_UNIT varchar(200),
	UPR_UNIT varchar(200),
	STOCK_UNIT varchar(200),
	EDIT_MASK varchar(200),
	GOODS_TB varchar(200),
	CALC_MTH varchar(200),
	REMARK varchar(500),
	PUCHAS_ACNT varchar(200),
	SELNG_ACNT varchar(200),
	STOCK_STDR varchar(200),
	THICK_STDR varchar(200),
	BT_LC varchar(200),
	RESVE_MTH varchar(200),
	CREATE_ID varchar(100),
	CREATE_DT date,
	UPDATE_ID varchar(100),
	UPDATE_DT date,
	PRIMARY KEY (GOODS_CD)
);


CREATE TABLE BM_MATERIAL_CODE
(
	MATERIAL_CD varchar(100) NOT NULL,
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
	CREATE_ID varchar(100),
	CREATE_DT date,
	UPDATE_ID varchar(100),
	UPDATE_DT date,
	PRIMARY KEY (MATERIAL_CD)
);



/* Create Foreign Keys */

ALTER TABLE BM_CUSTOMER_DIVISION
	ADD FOREIGN KEY (C_ID)
	REFERENCES BM_CUSTOMER_MAST (C_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE BM_CUSTOMER_SECURITY
	ADD FOREIGN KEY (C_ID)
	REFERENCES BM_CUSTOMER_MAST (C_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



