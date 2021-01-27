
/* Drop Tables */

DROP TABLE TB_SM01D01;
DROP TABLE TB_SM01H01;
DROP TABLE TB_SM01M01;
DROP TABLE TB_BM01M01;
DROP TABLE TB_SM02M01;




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
	PRDT_STKN varchar(10) NOT NULL,
	-- 내진설계 구분 : 내진설계만(S) 그외 SPACE
	PRDT_ERQK_CD char,
	-- 사이즈(스펙) : 철근:13~~29
	-- 기타
	PRDT_SIZE varchar(30) NOT NULL,
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
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (PRDT_CD)
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
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	-- 수입구분 : 수입:Y,  defaulf:N
	IMP_YN char DEFAULT 'N' NOT NULL,
	PRIMARY KEY (CLOSE_YM, CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
);


-- 재고변동이력
CREATE TABLE TB_SM01H01
(
	-- TRANSACTION일련번호
	TRST_NO numeric NOT NULL,
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
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
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
	CREAT_DTTM timestamp NOT NULL,
	-- 최종변경자
	UDT_ID varchar(20),
	-- 최종수정프로그램ID
	UPD_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (CO_CD, WH_CD, OWNER_CD, TYP_CD, MAKR_CD, PRJCT_CD, PRDT_CD, IMP_YN)
);


-- 재고이동현황 마스터
CREATE TABLE TB_SM02M01
(
	-- TRANSACTION 일련번호
	TRST_NO integer NOT NULL,
	-- 회사코드(출고) : 회사구분코드
	OUT_CO_CD varchar(10) NOT NULL,
	-- 창고구분(출고) : 공장,하치장,외주공장
	OUT_WH_CD varchar(10) NOT NULL,
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
	-- 회사코드(입고)
	IN_CO_CD varchar(10) NOT NULL,
	-- 창고구분(입고)
	IN_WH_CD varchar(10) NOT NULL,
	-- 재고단가 : 현재고단가
	STOCK_UPR numeric NOT NULL,
	-- 기준단가
	STD_UPR numeric NOT NULL,
	-- 최종매입단가 : 최종매입단가
	PCHS_UPR numeric NOT NULL,
	-- 최종매출단가 : 최종매출단가
	SELL_UPR numeric NOT NULL,
	-- 이동 중량
	MO_WH numeric NOT NULL,
	-- 비고
	RMK varchar(255),
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
	UPD_PGM varchar(30),
	-- 최종변경일시
	UDT_DTTM timestamp,
	PRIMARY KEY (TRST_NO)
);



/* Create Foreign Keys */

ALTER TABLE TB_SM01M01
	ADD FOREIGN KEY (PRDT_CD)
	REFERENCES TB_BM01M01 (PRDT_CD)
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



