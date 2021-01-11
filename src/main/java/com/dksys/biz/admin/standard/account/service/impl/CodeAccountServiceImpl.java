package com.dksys.biz.admin.standard.account.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.standard.account.mapper.CodeAccountMapper;
import com.dksys.biz.admin.standard.account.service.CodeAccountService;

@Service
public class CodeAccountServiceImpl implements CodeAccountService {
	
    @Autowired
    CodeAccountMapper codeMapper;

	@Override
	public List<Map<String, String>> selectCodeList() {
		return codeMapper.selectCodeList();
	}

	@Override
	public int insertCode(Map<String, String> param) {
		return codeMapper.insertCode(param);
	}

	@Override
	public int deleteCode(Map<String, String> param) {
		return codeMapper.deleteCode(param);
	}

	@Override
	public int updateCode(Map<String, String> param) {
		return codeMapper.updateCode(param);
	}

}