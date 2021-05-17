package com.dksys.biz.user.ar.ar09.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR09Mapper {
	
	int selectYySalesListCount(Map<String, String> paramMap);

	List<Map<String, String>> selectYySalesList(Map<String, String> paramMap);

	
}
