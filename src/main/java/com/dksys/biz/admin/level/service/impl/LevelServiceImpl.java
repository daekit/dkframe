package com.dksys.biz.admin.level.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.level.mapper.LevelMapper;
import com.dksys.biz.admin.level.service.LevelService;

@Service
@Transactional
public class LevelServiceImpl implements LevelService {
	
    @Autowired
    LevelMapper levelMapper;

	@Override
	public List<Map<String, String>> selectLevelList() {
		return levelMapper.selectLevelList();
	}

	@Override
	public int selectLevelCount(Map<String, String> paramMap) {
		return levelMapper.selectLevelCount(paramMap);
	}

	@Override
	public void insertLevel(Map<String, String> paramMap) {
		levelMapper.insertLevel(paramMap);
	}

	@Override
	public Map<String, String> selectLevelInfo(Map<String, String> paramMap) {
		return levelMapper.selectLevelInfo(paramMap);
	}

	@Override
	public void updateLevel(Map<String, String> paramMap) {
		levelMapper.updateLevel(paramMap);
	}

}