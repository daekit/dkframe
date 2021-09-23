package com.dksys.biz.user.sd.sd08.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd08.mapper.SD08Mapper;
import com.dksys.biz.user.sd.sd08.service.SD08Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD08SvcImpl implements SD08Svc {
	
    @Autowired
    SD08Mapper sd08Mapper;
    
	@Override
	public int selectCplrUntpcCount(Map<String, String> paramMap) {
		return sd08Mapper.selectCplrUntpcCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCplrUntpcList(Map<String, String> paramMap) {
		return sd08Mapper.selectCplrUntpcList(paramMap);
	}

	@Override
	public Map<String, String> selectCplrUntpc(Map<String, String> paramMap) {
		return sd08Mapper.selectCplrUntpc(paramMap);
	}

	@Override
	public int selectCplrUntpcKey(Map<String, String> paramMap) {
		return sd08Mapper.selectCplrUntpcKey(paramMap);
	}

	@Override
	public int insertCplrUntpc(Map<String, String> paramMap) {
		return sd08Mapper.insertCplrUntpc(paramMap);
	}

	@Override
	public int updateCplrUntpc(Map<String, String> paramMap) {
		return sd08Mapper.updateCplrUntpc(paramMap);
	}

	@Override
	public int deleteCplrUntpc(Map<String, String> paramMap) {
		return sd08Mapper.deleteCplrUntpc(paramMap);
	}
}