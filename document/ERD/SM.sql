
/* Drop Tables */

DROP TABLE TB_SM01D01;
DROP TABLE TB_SM01H01;
DROP TABLE TB_SM01M01;
DROP TABLE TB_BM01M01;
DROP TABLE TB_SD05M01;
DROP TABLE TB_SD06D01;
DROP TABLE TB_SD06D02;
DROP TABLE TB_SD06M01;




/* Create Tables */

-- 자재(제품)마스터
CREATE TABLE TB_BM01M01
(
	-- 제품코드 : 제품코드 (SD400)000001
	-- 시멘트0000002
	PRDT_CD varchar(10) NOT NULL,
	-- 제품명 : 철근(SD400)
	PRDT_NM varchar(200) NOT NULL,
	-- 제품구분 : 제고유형구분(철근, 형강, 건자재)
	PRDT_DIV varchar(10) NOT NULL,
	-- 제품강종 : SD400.SD500
	PRDT_STKN varchar(10),
	-- 사이즈(스펙)
	PRDT_SIZE varchar(30),
	-- 제품종류(일반,수입,내진)
	PRDT_KND char,
	-- 제품단위 : 처리단위(EA,KG,SH,매,본,장)
	PRDT_UNIT varchar(10),
	-- 제품단위중량
	PRDT_WT integer,
	-- 제품기준단가
	PRDT_UPR integer,
	-- 제품기준단가기준일자 : YYYYMMDD
	PRDT_DT char(8),
	-- 동국제강제품코드
	PRDT_DK_CD varchar(30),
	-- MES대응코드 : MES(폼묵코드+치수코드)
	PRDT_MES_CD varchar(30),
	-- 사용여부
	USE_YN char DEFAULT 'Y' NOT NULL,
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
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (PRDT_CD),
	CONSTRAINT MAT_MSTR_UX UNIQUE (PRDT_DIV, PRDT_STKN, PRDT_SIZE, PRDT_KND)
);


-- 프로젝트 마스터
CREATE TABLE TB_SD05M01
(
	-- 회사코드 : 회사구분코드
	CO_CD varchar(10) NOT NULL,
	-- 프로젝트 코드 : YYYY+MM+DD+SEQ(3)
	-- 자사재고 : 10000000001
	PRJCT_CD numeric DEFAULT 10000000001 NOT NULL,
	-- 턴키여부 : Y or N, Y일경우 재고관리 항목으로 사용됨
	TNKEY_YN char DEFAULT 'N' NOT NULL,
	-- 거래처 코드 : 계산서발행 판매처
	CLNT_CD integer NOT NULL,
	-- 제조사 : 제조사
	MAKR_CD varchar(10),
	-- 프로젝트 명
	PRJCT_NM varchar(100),
	-- 프로젝트 담당자
	MNG_ID varchar(20),
	-- 프로젝트 시작일자 : YYYYMMDD
	STRT_DT char(8),
	-- 프로젝트 종료 일자 : YYYYMMDD
	END_DT char(8),
	-- 총중량
	TOT_WT numeric,
	-- 주문번호 : YYYYMMDD+SEQ(3)
	ODR_NO numeric(11),
	-- 주문일자 : YYYYMMDD
	ODR_DT varchar(8),
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
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (CO_CD, PRJCT_CD)
);


-- 강종별 할증금액
CREATE TABLE TB_SD06D01
(
	-- 일련번호
	BASIS_PRICE_SEQ decimal(10) NOT NULL,
	-- 제품강종 : SD400.SD500
	PRDT_STKN varchar(10) NOT NULL,
	-- 할증금액
	SC_AMT integer NOT NULL,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (BASIS_PRICE_SEQ, PRDT_STKN)
);


-- 사이즈별 할증금액
CREATE TABLE TB_SD06D02
(
	-- 일련번호
	BASIS_PRICE_SEQ decimal(10) NOT NULL,
	-- 사이즈
	PRDT_SIZE varchar(30) NOT NULL,
	-- 할증금액
	SC_AMT integer,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (BASIS_PRICE_SEQ, PRDT_SIZE)
);


-- 자재(제품)기준단가
CREATE TABLE TB_SD06M01
(
	-- 일련번호
	BASIS_PRICE_SEQ decimal(10) NOT NULL,
	-- 제품기준단가기준일자 : YYYYMMDD
	PRDT_DT char(8) NOT NULL,
	-- 매입/매출구분 : 매입:1,  매출:2
	SELPCH_CD char NOT NULL,
	-- 제품종류(일반,수입,내진)
	PRDT_KND char NOT NULL,
	-- 제품구분 : 제고유형구분(철근, 형강, 건자재)
	PRDT_DIV varchar(10) NOT NULL,
	-- 제품기준단가
	PRDT_UPR integer NOT NULL,
	-- 생성자
	CREAT_ID varchar(20) NOT NULL,
	-- 생성프로그램ID
	CREAT_PGM varchar(30) NOT NULL,
	-- 생성일시
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (BASIS_PRICE_SEQ),
	UNIQUE (PRDT_DT, SELPCH_CD, PRDT_KND, PRDT_DIV)
);


-- 재고월마감자료
CREATE TABLE TB_SM01D01
(
	-- 마감년월 : 재고마감년월(YYYYMM)
	CLOSE_YM char(6) NOT NULL,
	-- 회사코드 : 회사구분코드
	CO_CD varchar(10) NOT NULL,
	-- 창고구분 : 공장,하치장,외주공장
	WH_CD varchar(10) NOT NULL,
	-- 재고주체구분 : 재고주체(자사,제강사,건설사,기타)
	OWNER_CD varchar(20) NOT NULL,
	-- 판매유형 : 판매유형(유통, 가공, 기타)
	TYP_CD varchar(10) NOT NULL,
	-- 제조사 : 제조사
	MAKR_CD varchar(10) NOT NULL,
	-- 프로젝트 코드 : YYYY+MM+DD+SEQ(3)
	-- 자사재고 : 10000000001
	PRJCT_CD numeric DEFAULT 10000000001 NOT NULL,
	-- 제품코드 : 제품코드 (SD400)000001
	-- 시멘트0000002
	PRDT_CD varchar(10) NOT NULL,
	-- 재고수량
	STOCK_QTY numeric NOT NULL,
	-- 재고단가 : 현재고단가
	STOCK_UPR numeric NOT NULL,
	-- 재고금액
	STOCK_AMT numeric NOT NULL,
	-- 기준단가
	STD_UPR numeric NOT NULL,
	-- 최종매입단가
	PCHS_UPR numeric NOT NULL,
	-- 최종매츨단가 : 최종출하단가
	SELL_UPR numeric NOT NULL,
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
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	-- 수입구분 : 수입:Y,  defaulf:N
	IMP_YN char DEFAULT 'N' NOT NULL,
	PRIMARY KEY (CLOSE_YM, CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
);


-- 재고변동이력
CREATE TABLE TB_SM01H01
(
	-- TRANSACTION일련번호
	TRST_NO numeric NOT NULL UNIQUE,
	-- 회사코드 : 회사구분코드
	CO_CD varchar(10) NOT NULL,
	-- 창고구분 : 공장,하치장,외주공장
	WH_CD varchar(10) NOT NULL,
	-- 재고주체구분 : 재고주체(자사,제강사,건설사,기타)
	OWNER_CD varchar(20) NOT NULL,
	-- 판매유형 : 판매유형(유통, 가공, 기타)
	TYP_CD varchar(10) NOT NULL,
	-- 제조사 : 제조사
	MAKR_CD varchar(10) NOT NULL,
	-- 프로젝트 코드 : YYYY+MM+DD+SEQ(3)
	-- 자사재고 : 10000000001
	PRJCT_CD numeric DEFAULT 10000000001 NOT NULL,
	-- 제품코드 : 제품코드 (SD400)000001
	-- 시멘트0000002
	PRDT_CD varchar(10) NOT NULL,
	-- 수입구분 : 수입:Y,  defaulf:N
	IMP_YN char DEFAULT 'N' NOT NULL,
	-- 처리일자 : 재고변경 처리일시
	PROC_DTTM datetime NOT NULL,
	-- 재고변동코드
	CHG_CD varchar(2) NOT NULL,
	-- 재고수량
	STOCK_QTY numeric NOT NULL,
	-- 재고단가 : 현재고단가
	STOCK_UPR numeric NOT NULL,
	-- 재고금액
	STOCK_AMT numeric NOT NULL,
	-- 기준단가
	STD_UPR numeric NOT NULL,
	-- 최종매입단가
	PCHS_UPR numeric NOT NULL,
	-- 최종매츨단가 : 최종출하단가
	SELL_UPR numeric NOT NULL,
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
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (TRST_NO)
);


-- 재고마스터
CREATE TABLE TB_SM01M01
(
	-- 회사코드 : 회사구분코드
	CO_CD varchar(10) NOT NULL,
	-- 창고구분 : 공장,하치장,외주공장
	WH_CD varchar(10) NOT NULL,
	-- 재고주체구분 : 재고주체(자사,제강사,건설사,기타)
	OWNER_CD varchar(20) NOT NULL,
	-- 판매유형 : 판매유형(유통, 가공, 기타)
	TYP_CD varchar(10) NOT NULL,
	-- 제조사 : 제조사
	MAKR_CD varchar(10) NOT NULL,
	-- 프로젝트 코드 : YYYY+MM+DD+SEQ(3)
	-- 자사재고 : 10000000001
	PRJCT_CD numeric DEFAULT 10000000001 NOT NULL,
	-- 제품코드 : 제품코드 (SD400)000001
	-- 시멘트0000002
	PRDT_CD varchar(10) NOT NULL,
	-- 수입구분 : 수입:Y,  defaulf:N
	IMP_YN char DEFAULT 'N' NOT NULL,
	-- 재고수량
	STOCK_QTY numeric NOT NULL,
	-- 재고단가 : 현재고단가
	STOCK_UPR numeric NOT NULL,
	-- 재고금액
	STOCK_AMT numeric NOT NULL,
	-- 기준단가
	STD_UPR numeric NOT NULL,
	-- 최종매입단가
	PCHS_UPR numeric NOT NULL,
	-- 최종매츨단가 : 최종출하단가
	SELL_UPR numeric NOT NULL,
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
	CREAT_DTTM datetime NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종병경일시
	UDT_DTTM datetime,
	PRIMARY KEY (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
);



/* Create Foreign Keys */

ALTER TABLE TB_SM01M01
	ADD FOREIGN KEY (PRDT_CD)
	REFERENCES TB_BM01M01 (PRDT_CD)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


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


ALTER TABLE TB_SM01D01
	ADD FOREIGN KEY (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
	REFERENCES TB_SM01M01 (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TB_SM01H01
	ADD FOREIGN KEY (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
	REFERENCES TB_SM01M01 (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



