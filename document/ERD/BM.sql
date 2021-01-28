
/* Drop Tables */

DROP TABLE TB_BM02D01;
DROP TABLE TB_BM02D02;
DROP TABLE TB_BM02M01;




/* Create Tables */

CREATE TABLE TB_BM02D01
(
	-- 사업부 순번
	BIZDEPT_SN integer NOT NULL,
	-- 10자리 : CLNT_000000 자동증가
	CLNT_CD varchar(10) NOT NULL,
	-- 사업부명
	BIZDEPT_NM varchar(25) NOT NULL,
	-- 사업부 구분 코드
	BIZDEPT_DIV_CD varchar(10),
	-- 담당자명
	MNG_NM varchar(10),
	-- 담당자 부서명
	MNG_DEPT_NM varchar(20),
	-- 담당자 직위코드
	MNG_PST_CD varchar(10),
	-- 담당자 전화번호
	MNG_TEL_NO varchar(13),
	-- 담당자 휴대전화번호
	MNG_MOBLPHON_NO varchar(13),
	-- 담당자 이메일
	MNG_EMAIL varchar(50),
	-- 영업 회사 코드
	SALES_CO_CD varchar(3),
	-- 영업 사원 ID
	SALES_EMP_ID varchar(15),
	-- 사용여부
	USE_YN char(1),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (BIZDEPT_SN)
);


CREATE TABLE TB_BM02D02
(
	-- 담보 내역 순번
	PLDG_SN integer NOT NULL,
	-- 10자리 : CLNT_000000 자동증가
	CLNT_CD varchar(10) NOT NULL,
	-- 회사 코드
	CO_CD varchar(3),
	-- 담보 구분
	PLDG_DIV_CD varchar(10),
	-- 담보 금액
	PLDG_AMT integer,
	-- 담보 인정 비율
	PLDG_RCOGN_RATE integer,
	-- 담보 비고
	PLDG_RMK varchar(255),
	-- 사용여부
	USE_YN char(1),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (PLDG_SN)
);


CREATE TABLE TB_BM02M01
(
	-- 10자리 : CLNT_000000 자동증가
	CLNT_CD varchar(10) NOT NULL,
	-- 거래처명
	CLNT_NM varchar(50) NOT NULL,
	-- 사업자 등록 번호
	CRN varchar(12),
	-- 매입/매출/운송 기타
	CLNT_DIV_CD varchar(10),
	-- 대표자명
	REPST_NM varchar(10),
	-- 업태 코드
	BIZCON_CD varchar(10),
	-- 종목 코드
	BSTY_CD varchar(10),
	-- 사업자 우편번호
	BIZ_ZIP varchar(6),
	-- 사업자 주소
	BIZ_ADDR varchar(30),
	-- 사업자 주소 상세
	BIZ_ADDR_DTL varchar(30),
	-- 사업자 전화번호
	BIZ_TEL_NO varchar(13),
	-- 사업자 FAX 번호
	BIZ_FAX_NO varchar(13),
	-- 보조 메모 URL
	SUB_RMK_URL varchar(30),
	-- 은행 코드
	BANK_CD varchar(10),
	-- 은행 계좌 번호
	BKAC_NO varchar(20),
	BANK_MBER_NM varchar(10),
	-- 기본 여신 금액
	BASIS_CDTLN_AMT integer,
	PCHS_PAY_DIV_CD varchar(10),
	PCHS_CLMN_DIV_CD varchar(10),
	PCHS_PMNT_MTD_CD varchar(10),
	PCHS_VAT_DIV_CD varchar(10),
	SELL_PAY_DIV_CD varchar(10),
	SELL_CLMN_DIV_CD varchar(10),
	SELL_PMNT_MTD_CD varchar(10),
	SELL_VAT_DIV_CD varchar(10),
	-- 거래처 메모
	CLNT_RMK varchar(1000),
	-- 사용여부
	USE_YN char(1),
	-- 여분필드(고정)
	ETC_FIELD1 char(10),
	-- 숫자여분필드
	ETC_FIELD2 numeric,
	-- 여분필드(가변)
	ETC_FIELD3 varchar(100),
	CREAT_ID varchar(20) NOT NULL,
	CREAT_PGM varchar(30) NOT NULL,
	CREAT_DTTM timestamp NOT NULL,
	UDT_ID varchar(20),
	UDT_PGM varchar(30),
	UDT_DTTM timestamp,
	PRIMARY KEY (CLNT_CD)
);



/* Create Foreign Keys */

ALTER TABLE TB_BM02D01
	ADD FOREIGN KEY (CLNT_CD)
	REFERENCES TB_BM02M01 (CLNT_CD)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_BM02D02
	ADD FOREIGN KEY (CLNT_CD)
	REFERENCES TB_BM02M01 (CLNT_CD)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



