package com.dksys.biz.user.sm.sm02.service;

import java.util.List;
import java.util.Map;

import com.dksys.biz.cmn.vo.PaginationInfo;

public interface SM02Svc {

	public List<Map<String, String>> selectCmnCodeList(Map<String, String> param);
	
	public List<Map<String, String>> selectDtlCmnCodeList(Map<String, String> param);

	public List<Map<String, String>> selectClntSearchList(Map<String, String> param);

	public List<Map<String, String>> selectPrdtSearchList(Map<String, String> param);
	
	public int selectUprCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockMoveStatMngmList(Map<String, String> param);
	    
	public int selectUprDtlCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param);
	
	public int sm02UpdateTernKeyStockMst(Map<String, String> param);
	
	public int selectStockTernKeykMovePchListCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockTernKeykMovePchList(Map<String, String> param);
	
	public List<Map<String, String>> sm02selectTernKeyStock(Map<String, String> param);
	
	/* public int sm01CheckCnt(Map<String, Object> param); */
	
	/* public int sm01InsertStockMove(Map<String, String> param); */
	
	
	public int sm01UpdateInsertStockMove(Map<String, String> param);
	
	public int insertUpdateTernKeyStockMove(Map<String, String> param);

	public int sm01UpdateInsertBarterStockMove(Map<String, String> param);
	
	boolean checkStockClose(Map<String, String> paramMap);

	public int insertIfMesStockMove(Map<String, String> detailMap);
	
	public int updateStockMoveRmk(Map<String, String> param);
	
	public int updateStockMoveCaryng(Map<String, String> param);
	
}
