package com.dksys.biz.user.sm.sm04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM04Mapper {
	
	int selectPrdtAcinsCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPrdtAcinsList(Map<String, String> paramMap);
	
	Map<String, String> selectPrdtAcinsInfo(Map<String, String> paramMap);
	
	int insertPrdtAcins(Map<String, String> param);

	int updatePrdtAcins(Map<String, String> param);
	
	int copyInsert(Map<String, String> param);
	
	int deletePrdtAcins(Map<String, String> param);

	int deleteCopy(Map<String, String> param);
	
}
