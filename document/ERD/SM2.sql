
/* Drop Tables */

DROP TABLE TB_SM02M01;




/* Create Tables */

-- 재고이동현황 마스터
CREATE TABLE TB_SM02M01
(
	-- 재고이동현황 마스터 KEY
	MO_KEY integer NOT NULL,
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
	-- 이동 중량
	MO_WH numeric NOT NULL,
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
	PRIMARY KEY (MO_KEY)
);



