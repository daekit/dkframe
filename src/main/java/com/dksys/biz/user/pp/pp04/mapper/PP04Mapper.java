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

	Map<String, String> selectAr01MList(Map<String, String> paramMap);

	List<Map<String, String>> selectAr01DList(Map<String, String> paramMap);
	
	int insertMesList(Map<String, String> paramMap);
	
	int insertMesDetailList(Map<String, String> paramMap);
	
	int insertSellTrst(Map<String, String> paramMap);
	
	int updateMesMtrlRslt(Map<String, String> paramMap);
	
	int updateMesListAmt(Map<String, String> paramMap);

	int selectBilgNoCnt(Map<String, String> paramMap);
	
	int deleteMesList(Map<String, String> paramMap);
	
	int deleteMesDetailList(Map<String, String> paramMap);
	
	int deleteSellTrst(Map<String, String> paramMap);

	Map<String, Object> selectAr01MListLoadNo(Map<String, String> listMap);

	void updateAr01M(Map<String, Object> listMap);

	void insertAr01M(Map<String, String> listMap);

	void insertShip(Map<String, String> map);

	Map<String, String> selectSite(Map<String, String> map);

	int selectMesMtrlRstlFirstCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesMtrlRstlFirstList(Map<String, String> paramMap);

	Map<String, String> insertOrder(Map<String, String> map);

	Map<String, String> insertOrderDetail(Map<String, String> map);

	void insertShipDetail(Map<String, String> map);

	Map<String, String> selectStockInfo(Map<String, String> stockMap);

	Map<String, String> selectAr01MSeq();

	List<Map<String, String>> daliyAccessList(Map<String, String> paramMap);

	void updateMesErpFlag(Map<String, String> map);
	
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
