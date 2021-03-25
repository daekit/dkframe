package com.dksys.biz.main.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.main.mapper.LoginMapper;
import com.dksys.biz.main.service.LoginService;
import com.dksys.biz.main.vo.User;

@Service
@SuppressWarnings("all")
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginMapper loginMapper;
	
	@Override
	public User selectUserInfo(Map<String, String> param) {
		return loginMapper.selectUserInfo(param);
	}

	@Override
	public int insertUserHistory(User user) {
		return loginMapper.insertUserHistory(user);
	}

}