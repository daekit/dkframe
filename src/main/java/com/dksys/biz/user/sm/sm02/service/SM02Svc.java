package com.dksys.biz.user.sm.sm02.service;

import java.util.List;
import java.util.Map;

public interface SM02Svc {

	public List<Map<String, String>> selectCmnCodeList(Map<String, String> param);
	
	public List<Map<String, String>> selectDtlCmnCodeList(Map<String, String> param);

	public List<Map<String, String>> selectClntSearchList(Map<String, String> param);

	public List<Map<String, String>> selectPrdtSearchList(Map<String, String> param);
	
	public int selectUprCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockMoveStatMngmList(Map<String, String> param);
	    
	public int selectUprDtlCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param);
	
	public int sm01CheckCnt(Map<String, String> param);
	
	public int sm01InsertStockMove(Map<String, String> param);
	
	public int sm01UpdateStockMove(Map<String, String> param);
	
	public int sm01UpdateInsertStockMove(Map<String, String> param);
	
	public int sm02InsertStockMove(Map<String, String> param);
	
}