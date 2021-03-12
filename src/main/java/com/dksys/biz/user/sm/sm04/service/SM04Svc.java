package com.dksys.biz.user.sm.sm04.service;

import java.util.List;
import java.util.Map;

public interface SM04Svc {

	int selectPrdtAcinsCount(Map<String, String> param);

	List<Map<String, String>> selectPrdtAcinsList(Map<String, String> param);

	Map<String, String> selectPrdtAcinsInfo(Map<String, String> paramMap);
	
	int insertPrdtAcins(Map<String, String> param);

	int updatePrdtAcins(Map<String, String> param);
	
	int copyInsert(Map<String, String> param);

	int deletePrdtAcins(Map<String, String> param);

	int deleteCopy(Map<String, String> param);

}