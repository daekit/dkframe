package com.dksys.biz.admin.user.service;

import java.util.List;
import java.util.Map;

public interface UserService {

	public List<Map<String, String>> selectUserList(Map<String, String> paramMap);

	public int selectUserCount(Map<String, String> paramMap);

	public void insertUser(Map<String, String> paramMap) throws Exception;

	public Map<String, String> selectUserInfo(Map<String, String> paramMap);

	public void updateUser(Map<String, String> paramMap) throws Exception;

}