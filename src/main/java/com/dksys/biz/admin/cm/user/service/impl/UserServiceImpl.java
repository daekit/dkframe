package com.dksys.biz.admin.cm.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.user.mapper.UserMapper;
import com.dksys.biz.admin.cm.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
    @Autowired
    UserMapper userMapper;

	@Override
	public List<Map<String, String>> selectUserList(Map<String, String> paramMap) {
		return userMapper.selectUserList(paramMap);
	}

	@Override
	public int selectUserCount(Map<String, String> paramMap) {
		return userMapper.selectUserCount(paramMap);
	}

	@Override
	public void insertUser(Map<String, String> paramMap) throws Exception{
		userMapper.insertUser(paramMap);
	}

	@Override
	public Map<String, String> selectUserInfo(Map<String, String> paramMap) {
		return userMapper.selectUserInfo(paramMap);
	}

	@Override
	public void updateUser(Map<String, String> paramMap) throws Exception {
		userMapper.updateUser(paramMap);
	}

}