package com.dksys.biz.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
	
	List<Map<String, String>> selectAuthList();

	int insertAuth(Map<String, String> param);

	int deleteAuth(Map<String, String> param);

	int updateAuth(Map<String, String> param);
	
}
