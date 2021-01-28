package com.dksys.biz.admin.cm.cm07.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM07Mapper {
	
	List<Map<String, String>> selectLevelList(Map<String, String> paramMap);

	int selectLevelCount(Map<String, String> paramMap);

	int insertLevel(Map<String, String> paramMap);

	Map<String, String> selectLevelInfo(Map<String, String> paramMap);

	int updateLevel(Map<String, String> paramMap);
	
}
