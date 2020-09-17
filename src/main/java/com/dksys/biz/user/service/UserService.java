package com.dksys.biz.user.service;

import java.util.List;
import java.util.Map;

import com.dksys.biz.user.vo.User;

public interface UserService {

    public List<User> selectAllUser();

	public List<Map<String, String>> selectUserList(Map<String, String> paramMap);

	public int selectUserCount(Map<String, String> paramMap);

	public void createUser(Map<String, String> paramMap) throws Exception;

	public Map<String, String> selectUserInfo(Map<String, String> paramMap);

	public void updateUser(Map<String, String> paramMap) throws Exception;

}