package com.dksys.biz.user.sm.sm06.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM06Mapper {
	
	int selectStockTotalListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockTotalList(Map<String, String> param);
	
	int selectStockDetailListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockDetailList(Map<String, String> param);
}
