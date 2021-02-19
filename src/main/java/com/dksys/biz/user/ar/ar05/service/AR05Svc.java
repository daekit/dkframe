package com.dksys.biz.user.ar.ar05.service;

import java.util.List;
import java.util.Map;

public interface AR05Svc {

	int selectEtrdpsCount(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsList(Map<String, String> paramMap);

	Map<String, Object> selectEtrdpsInfo(Map<String, String> paramMap);
	
	void insertEtrdps(Map<String, Object> paramMap);
	
	void updateEtrdps(Map<String, Object> paramMap);

	void deleteEtrdps(Map<String, String> paramMap);
	
}