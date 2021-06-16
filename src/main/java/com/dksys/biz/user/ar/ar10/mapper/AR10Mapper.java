package com.dksys.biz.user.ar.ar10.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR10Mapper {
	int selectPchsSellCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> selectPchsSellList(Map<String, String> paramMap);

	int selectPchsSellSumCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> selectPchsSellSumList(Map<String, String> paramMap);
}
