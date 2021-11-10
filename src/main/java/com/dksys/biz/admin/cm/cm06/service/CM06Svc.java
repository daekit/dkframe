package com.dksys.biz.admin.cm.cm06.service;

import java.util.List;
import java.util.Map;

public interface CM06Svc {

	public int selectUserCount(Map<String, String> paramMap);
	
	public List<Map<String, String>> selectUserList(Map<String, String> paramMap);

	public Map<String, String> selectUserInfo(Map<String, String> paramMap);

	public List<Map<String, String>> selectUserTree(Map<String, String> paramMap);
	
	public void insertUser(Map<String, String> paramMap) throws Exception;
	
	public void updateUser(Map<String, String> paramMap) throws Exception;

	public int insertPgmHistory(Map<String, String> paramMap);

	public int updatePw(Map<String, String> paramMap);
	
	public Map<String, String> updatePwErrCnt(Map<String, String> paramMap);
	
	public List<Map<String, String>> selectRuleCheckList(Map<String, String> paramMap);
	
	/* 테스트를 위한 코드이므로 추후 삭제할것. */
//	 public void updateUserName() throws Exception;

}