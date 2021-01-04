package com.dksys.biz.admin.mat.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.mat.mapper.MatMapper;
import com.dksys.biz.admin.mat.service.MatService;

@Service
@Transactional
public class MatServiceImpl implements MatService {
	
    @Autowired
    MatMapper matMapper;

	@Override
	public List<Map<String, String>> selectMatList() {
		return matMapper.selectMatList();
	}

	@Override
	public int insertMat(Map<String, String> param) {
		return matMapper.insertMat(param);
	}

	@Override
	public int deleteMat(Map<String, String> param) {
		return matMapper.deleteMat(param);
	}

	@Override
	public int updateMat(Map<String, String> param) {
		return matMapper.updateMat(param);
	}

	@Override
	public Map<String, String> selectMatInfo(Map<String, String> paramMap) {
		return matMapper.selectMatInfo(paramMap);
	}
}