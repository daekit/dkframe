package com.dksys.biz.user.pp.pp04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PP04Mapper {
	


	int selectMesMtrlRstlCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesMtrlRstlList(Map<String, String> paramMap);

	List<Map<String, String>> selectMesAllocVehlDtlList(Map<String, String> paramMap);

	List<Map<String, String>> selectOdrList(Map<String, Object> paramMap);
	
	int insertMesList(Map<String, String> paramMap);
	
	int insertMesDetailList(Map<String, String> paramMap);
	
	int insertSellTrst(Map<String, String> paramMap);
	
	int updateMesMtrlRslt(Map<String, String> paramMap);
	
	int updateMesListAmt(Map<String, String> paramMap);

	int selectBilgNoCnt(Map<String, String> paramMap);
	
	int deleteMesList(Map<String, String> paramMap);
	
	int deleteMesDetailList(Map<String, String> paramMap);
	
	int deleteSellTrst(Map<String, String> paramMap);
	
	/*
	 * 
	 * 	int selectMesShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesShipList(Map<String, String> paramMap);
	


	int updatMesSHip(Map<String, String> param);
	
	int deleteMesShip(Map<String, String> param);

	int insertMesShip(Map<String, String> param);
	
	int updatMesSHipConfirm(Map<String, String> param);
	
	List<Map<String, String>> selectMesAllocVehlList(Map<String, String> paramMap);

	int selectMesAllocVehlCount(Map<String, String> param);


	
	*/
	 
//	Map<String, String> selectPrdtAcinsInfo(Map<String, String> paramMap);
	
//	int copyInsert(Map<String, String> param);

//	int deleteCopy(Map<String, String> param);
	
	
}
