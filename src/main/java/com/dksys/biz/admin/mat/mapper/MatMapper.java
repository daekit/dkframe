package com.dksys.biz.admin.mat.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatMapper {
	
	List<Map<String, String>> selectMatList();

	int insertMat(Map<String, String> param);

	int deleteMat(Map<String, String> param);

	int updateMat(Map<String, String> param);

	Map<String, String> selectMatInfo(Map<String, String> paramMap);
	
}
