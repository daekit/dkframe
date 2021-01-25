
/* Drop Tables */

DROP TABLE TB_SD05M01;




/* Create Tables */

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
	-- 최종변경일시
	UDT_DTTM datetime,
	PRIMARY KEY (CO_CD, PRJCT_CD)
);



