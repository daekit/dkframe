package com.dksys.biz.admin.cm.cm06.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM06Mapper {
	
	List<Map<String, String>> selectUserList(Map<String, String> paramMap);

	int selectUserCount(Map<String, String> paramMap);

	int insertUser(Map<String, String> paramMap);

	Map<String, String> selectUserInfo(Map<String, String> paramMap);

	int updateUser(Map<String, String> paramMap);
	
}
