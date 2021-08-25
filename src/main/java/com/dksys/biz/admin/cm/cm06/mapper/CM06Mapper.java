package com.dksys.biz.admin.cm.cm06.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM06Mapper {
	
	int selectUserCount(Map<String, String> paramMap);

	List<Map<String, String>> selectUserList(Map<String, String> paramMap);

	Map<String, String> selectUserInfo(Map<String, String> paramMap);
	
	List<Map<String, String>> selectUserTree(Map<String, String> paramMap);

	int insertUser(Map<String, String> paramMap);
	
	int updateUser(Map<String, String> paramMap);

	int insertUserOauth(Map<String, String> paramMap);

	int insertPgmHistory(Map<String, String> paramMap);

	int updatePw(Map<String, String> paramMap);

	int updateTokenPw(Map<String, String> paramMap);

	List<Map<String, String>> selectRuleCheckList(Map<String, String> paramMap); 

	int updatePwErrCnt(Map<String, String> paramMap);

	void updateUserN(Map<String, String> paramMap);
	
}
