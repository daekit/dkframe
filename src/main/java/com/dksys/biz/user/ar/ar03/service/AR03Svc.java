package com.dksys.biz.user.ar.ar03.service;

import java.util.List;
import java.util.Map;

public interface AR03Svc {

	int selectCaryngCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectCaryngList(Map<String, String> paramMap);
	
	Map<String, String> selectCaryngInfo(Map<String, String> paramMap);
	
	int selectShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipList(Map<String, String> paramMap);
	
	public int insertCaryng(Map<String, String> param);
	
	public int updateCaryng(Map<String, String> param);
	
	int deleteTrans(Map<String, String> paramMap);

	public int updateProcYn(Map<String, Object> param);
	
}