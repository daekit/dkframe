package com.dksys.biz.admin.cm.level.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.level.mapper.LevelMapper;
import com.dksys.biz.admin.cm.level.service.LevelService;

@Service
public class LevelServiceImpl implements LevelService {
	
    @Autowired
    LevelMapper levelMapper;

	@Override
	public List<Map<String, String>> selectLevelList(Map<String, String> paramMap) {
		return levelMapper.selectLevelList(paramMap);
	}

	@Override
	public int selectLevelCount(Map<String, String> paramMap) {
		return levelMapper.selectLevelCount(paramMap);
	}

	@Override
	public void insertLevel(Map<String, String> paramMap) throws Exception{
		levelMapper.insertLevel(paramMap);
	}

	@Override
	public Map<String, String> selectLevelInfo(Map<String, String> paramMap) {
		return levelMapper.selectLevelInfo(paramMap);
	}

	@Override
	public void updateLevel(Map<String, String> paramMap) throws Exception{
		levelMapper.updateLevel(paramMap);
	}

}