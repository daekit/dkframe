package com.dksys.biz.user.ar.ar02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR02Mapper {

	int insertPchsSell(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);

	int selectSellCount(Map<String, String> paramMap);

	
}