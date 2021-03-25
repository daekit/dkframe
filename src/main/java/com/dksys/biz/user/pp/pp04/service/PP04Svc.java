package com.dksys.biz.user.pp.pp04.service;

import java.util.List;
import java.util.Map;

public interface PP04Svc {

	int selectMesShipCount(Map<String, String> param);

	List<Map<String, String>> selectMesShipList(Map<String, String> param);
	
	int insertMesShip(Map<String, String> param);

	int updatMesSHip(Map<String, String> param);

	int deleteMesShip(Map<String, String> param);
	
	int updatMesSHipConfirm(Map<String, String> param);

	List<Map<String, String>> selectMesAllocVehlList(Map<String, String> paramMap);

	int selectMesMtrlRstlCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesMtrlRstlList(Map<String, String> paramMap);

	
}