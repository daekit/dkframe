package com.dksys.biz.user.sm.sm02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM02Mapper {
	
	List<Map<String, String>> selectCmnCodeList(Map<String, String> param);
	
	List<Map<String, String>> selectDtlCmnCodeList(Map<String, String> param);

	List<Map<String, String>> selectClntSearchList(Map<String, String> param);

	List<Map<String, String>> selectPrdtSearchList(Map<String, String> param);
	
	int selectUprCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockMoveStatMngmList(Map<String, String> param);
	
	int selectUprDtlCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param);
	
	/* int sm01CheckCnt(Map<String, Object> param); */
	
	/* int sm01InsertStockMove(Map<String, Object> param); */

	int sm01UpdateStockMove(Map<String, String> param);

	int sm01UpdateInsertStockMove(Map<String, String> detailMap);
	
	int sm02InsertStockMove(Map<String, String> param);
	
}
