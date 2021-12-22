package com.dksys.biz.douzone.dz.dz01.service;

import java.util.List;
import java.util.Map;

public interface DZ01Svc {
	List<Map<String, String>> testSelect(Map<String, String> paramMap);
	
	public void dzInsert(Map<String, String> paramMap);
	
	Map<String, String> getAcctAm(Map<String, String> paramMap);
	
	Map<String, String> checkTrstCertiNo(Map<String, String> paramMap);
}