package com.dksys.biz.admin.bm.account.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.bm.account.mapper.AccountMapper;
import com.dksys.biz.admin.bm.account.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
    @Autowired
    AccountMapper accountMapper;


	@Override
	public int selectCodeCount(Map<String, String> param) {
		return accountMapper.selectCodeCount(param);
	}

	@Override
	public List<Map<String, String>> selectCodeList(Map<String, String> param) {
		return accountMapper.selectCodeList(param);
	}

	@Override
	public int insertCode(Map<String, String> param) {
		return accountMapper.insertCode(param);
	}

	@Override
	public int deleteCode(Map<String, String> param) {
		return accountMapper.deleteCode(param);
	}

	@Override
	public int updateCode(Map<String, String> param) {
		return accountMapper.updateCode(param);
	}



}