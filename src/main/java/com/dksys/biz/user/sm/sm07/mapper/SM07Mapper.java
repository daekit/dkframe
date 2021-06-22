package com.dksys.biz.user.sm.sm07.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM07Mapper {
	
	int selectStockSummaryListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockSummaryList(Map<String, String> param);
	
	int selectStockSummaryDetailListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockSummaryDetailList(Map<String, String> param);
	
	String selectSearchDttm();

}
