package com.dksys.biz.user.pp.pp04.service;

import java.util.List;
import java.util.Map;

public interface PP04Svc {


	
	
	/* 여기서 부터 MES에서 가져오는 함수즐  */
	int selectMesMtrlRstlCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectMesMtrlRstlList(Map<String, String> paramMap);

	List<Map<String, String>> selectMesAllocVehlDtlList(Map<String, String> paramMap);	

	int insertMesShipList(Map<String, Object> param);

	
	
	/*
	 * 
	 * 
	int selectMesShipCount(Map<String, String> param);

	List<Map<String, String>> selectMesShipList(Map<String, String> param);
	

	int updatMesSHip(Map<String, String> param);

	int deleteMesShip(Map<String, String> param);	

	int updatMesSHipConfirm(Map<String, String> param);
	
	*/
	
}