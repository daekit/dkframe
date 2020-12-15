package com.dksys.biz.admin.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.code.mapper.CodeMapper;
import com.dksys.biz.admin.code.service.CodeService;

@Service
@Transactional
public class CodeServiceImpl implements CodeService {
	
    @Autowired
    CodeMapper codeMapper;

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

	@Override
	public List<Map<String, String>> selectCodeInfoList(Map<String, String> param) {
		return codeMapper.selectCodeInfoList(param);
	}

}