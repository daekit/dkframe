package com.dksys.biz.douzone.dz.dz01.service;

import java.util.List;
import java.util.Map;

public interface DZ01Svc {
	List<Map<String, String>> testSelect(Map<String, String> paramMap);
	
	public int dzInsert(Map<String, String> paramMap);
	
	Map<String, String> getAcctAm(Map<String, String> paramMap);
	
	Map<String, String> checkTrstCertiNo(Map<String, String> paramMap);
	
	int getCountInSeq(Map<String, String> paramMap);
	
	String getTrCd(Map<String, String> paramMap);

	public int dzDelete(Map<String, String> paramMap);
	
	public int getDzTrade(Map<String, String> paramMap);
	
}