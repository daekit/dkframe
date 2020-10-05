package com.dksys.biz.admin.level.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LevelMapper {
	
	List<Map<String, String>> selectLevelList(Map<String, String> paramMap);

	int selectLevelCount(Map<String, String> paramMap);

	int insertLevel(Map<String, String> paramMap);

	Map<String, String> selectLevelInfo(Map<String, String> paramMap);

	int updateLevel(Map<String, String> paramMap);
	
}
