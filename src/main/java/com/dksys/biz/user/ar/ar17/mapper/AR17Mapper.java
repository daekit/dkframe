package com.dksys.biz.user.ar.ar17.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR17Mapper {

	int selectTrmendAmtListCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTrmendAmtList(Map<String, String> paramMap);
	
}
