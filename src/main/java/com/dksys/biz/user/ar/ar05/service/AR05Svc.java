package com.dksys.biz.user.ar.ar05.service;

import java.util.List;
import java.util.Map;

public interface AR05Svc {

	int selectEtrdpsCount(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsList(Map<String, String> paramMap);

	Map<String, Object> selectEtrdpsInfo(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsDtlList(Map<String, String> paramMap);
	
	int insertEtrdps(Map<String, Object> paramMap);
	
	int updateEtrdps(Map<String, Object> paramMap);

	int deleteEtrdps(Map<String, String> paramMap);
	
	int calcelMatch(Map<String, String> paramMap);

	boolean checkEtrdpsClose(Map<String, String> paramMap);

	int selectEtrdpsMapCount(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsMap(Map<String, String> paramMap);
	
	//더존 연동 관련 데이터 update 20220630 kdm 
	int updateDzSndSeq(Map<String, String> paramMap);
	
}