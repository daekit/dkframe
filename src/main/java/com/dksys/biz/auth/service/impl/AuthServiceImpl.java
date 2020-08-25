package com.dksys.biz.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.auth.mapper.AuthMapper;
import com.dksys.biz.auth.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
	
    @Autowired
    AuthMapper authMapper;

	@Override
	public List<Map<String, String>> selectAuthList() {
		return authMapper.selectAuthList();
	}

}