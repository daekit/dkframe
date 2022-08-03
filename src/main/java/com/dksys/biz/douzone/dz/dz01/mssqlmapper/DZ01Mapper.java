package com.dksys.biz.douzone.dz.dz01.mssqlmapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface DZ01Mapper {
	
	List<Map<String, String>> testSelect(Map<String, String> paramMap);
	
	Map<String, String> getAcctAm(Map<String, String> paramMap);
	
	int dzInsert(Map<String, String> paramMap);

	Map<String, String> checkTrstCertiNo(Map<String, String> paramMap);

	int getCntSeq(Map<String, String> paramMap);

	String getTrCd(Map<String, String> paramMap);


	void dzDelete(Map<String, String> paramMap);
	
	int getDzCnt(Map<String, String> paramMap);

	int getDzTrade(Map<String, String> paramMap);
	
}