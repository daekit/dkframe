package com.dksys.biz.user.ar.ar03.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR03Mapper {

	int selectCaryngCount(Map<String, String> paramMap);

	List<Map<String, String>> selectCaryngList(Map<String, String> paramMap);
	
	Map<String, String> selectCaryngInfo(Map<String, String> paramMap);
	
	int selectShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipList(Map<String, String> paramMap);
	
	int insertCaryng(Map<String, String> param);
	
	int updateCaryng(Map<String, String> param);
	
	int deleteTrans(Map<String, String> paramMap);

	int updateProcYn(Map<String, String> detailMap);
}