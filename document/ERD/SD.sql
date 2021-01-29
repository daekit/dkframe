
/* Drop Tables */

DROP TABLE TB_SD05M01;
DROP TABLE TB_SD06D01;
DROP TABLE TB_SD06D02;
DROP TABLE TB_SD06M01;




/* Create Tables */

-- 프로젝트 마스터
CREATE TABLE TB_SD05M01
(
	-- 회사코드 : 회사구분코드
	CO_CD varchar(20) NOT NULL,
	-- 프로젝트 코드 : YYYY+MM+DD+SEQ(3)
	-- 자사재고 : 10000000001
	PRJCT_CD numeric DEFAULT 10000000001 NOT NULL,
	-- 턴키여부 : Y or N, Y일경우 재고관리 항목으로 사용됨
	TNKEY_YN varchar(20) DEFAULT 'N' NOT NULL,
	-- 거래처 코드 : 계산서발행 판매처
	CLNT_CD varchar(10) NOT NULL,
	-- 제조사 : 제조사
	MAKR_CD varchar(20),
	-- 프로젝트 명
	PRJCT_NM varchar(100),
	-- 프로젝트 담당자
	MNG_ID varchar(20),
	-- 프로젝트 시작일자 : YYYYMMDD
	STRT_DT varchar(8),
	-- 프로젝트 종료 일자 : YYYYMMDD
	END_DT varchar(8),
	-- 총중량
	TOT_WT numeric(15,3),
	-- 비고
	RMK varchar(1000),
	-- 여분필드(고정) : 여분필드(고정)
	ETC_FIELD1 char(10),
	-- 여분필드(숫자) : 숫자여분필드
	ETC_FIELD2 numeric,
	-- 여분필드(가변) : 여분필드(가변)
	ETC_FIELD3 varchar(100),
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UDT_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (CO_CD, PRJCT_CD)
);


-- 강종별 할증금액
CREATE TABLE TB_SD06D01
(
	-- 일련번호 : TB_SD06M01_SQ01
	BASIS_PRICE_SEQ numeric(10) NOT NULL,
	-- 제품강종 : SD400.SD500
	PRDT_STKN varchar(20) NOT NULL,
	-- 내진설계 구분 : 내진설계만(S) 그외 SPACE
	PRDT_ERQK_CD varchar(20) NOT NULL,
	-- 할증금액
	SC_AMT integer NOT NULL,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UDT_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (BASIS_PRICE_SEQ, PRDT_STKN, PRDT_ERQK_CD)
);


-- 사이즈별 할증금액
CREATE TABLE TB_SD06D02
(
	-- 일련번호 : TB_SD06M01_SQ01
	BASIS_PRICE_SEQ numeric(10) NOT NULL,
	-- 사이즈
	PRDT_SIZE varchar(30) NOT NULL,
	-- 내진설계 구분 : 내진설계만(S) 그외 SPACE
	PRDT_ERQK_CD varchar(20) NOT NULL,
	-- 할증금액
	SC_AMT integer,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UDT_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (BASIS_PRICE_SEQ, PRDT_SIZE, PRDT_ERQK_CD)
);


-- 자재(제품)기준단가
CREATE TABLE TB_SD06M01
(
	-- 제품기준단가기준일자 : YYYYMMDD
	PRDT_DT char(8) NOT NULL,
	-- 매입/매출구분 : 매입:1,  매출:2
	SELPCH_CD varchar(20) NOT NULL,
	-- 제품종류(일반,수입)
	PRDT_IMP_YN varchar(20) NOT NULL,
	-- 제품구분 : 제고유형구분(철근, 형강, 건자재)
	PRDT_DIV varchar(20) NOT NULL,
	-- 제품기준단가
	PRDT_UPR numeric(12,2) NOT NULL,
	-- 일련번호 : TB_SD06M01_SQ01
	BASIS_PRICE_SEQ numeric(10) NOT NULL UNIQUE,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UDT_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (PRDT_DT, SELPCH_CD, PRDT_IMP_YN, PRDT_DIV),
	UNIQUE (PRDT_DT, SELPCH_CD, PRDT_IMP_YN, PRDT_DIV)
);



/* Create Foreign Keys */

ALTER TABLE TB_SD06D01
	ADD FOREIGN KEY (BASIS_PRICE_SEQ)
	REFERENCES TB_SD06M01 (BASIS_PRICE_SEQ)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_SD06D02
	ADD FOREIGN KEY (BASIS_PRICE_SEQ)
	REFERENCES TB_SD06M01 (BASIS_PRICE_SEQ)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



