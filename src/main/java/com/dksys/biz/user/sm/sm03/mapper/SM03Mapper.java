package com.dksys.biz.user.sm.sm03.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM03Mapper {
	
	int selectStockListCount(Map<String, String> param);
	
	List<Map<String, String>> selectStockList(Map<String, String> param);
	
}
