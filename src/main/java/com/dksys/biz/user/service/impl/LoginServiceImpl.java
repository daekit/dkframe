package com.dksys.biz.user.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.mapper.LoginMapper;
import com.dksys.biz.user.service.LoginService;
import com.dksys.biz.user.vo.User;

@Service
@SuppressWarnings("all")
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginMapper loginMapper;
	
	@Override
	public User selectUserInfo(Map<String, String> param) {
		return loginMapper.selectUserInfo(param);
	}

}