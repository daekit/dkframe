package com.dksys.biz.user.sm.sm01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM01Mapper {
	
	int selectStockListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockList(Map<String, String> param);

	int selectStockHistoryListCount(Map<String, String> param);

	List<Map<String, String>> selectStockHistoryList(Map<String, String> param);

	Map<String, String> selectStockInfo(Map<String, String> paramMap);

	int updateStockSell(Map<String, String> paramMap);

	int updateStockCancel(Map<String, String> paramMap);

	int updateStockUpr(Map<String, String> paramMap);

	

}
