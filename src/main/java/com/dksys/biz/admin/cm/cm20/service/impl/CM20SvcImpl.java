package com.dksys.biz.admin.cm.cm20.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm20.mapper.CM20Mapper;
import com.dksys.biz.admin.cm.cm20.service.CM20Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class CM20SvcImpl implements CM20Svc {
	
    @Autowired
    CM20Mapper cm20Mapper;
    

	@Override
	public List<Map<String, String>> selectSanctnList(Map<String, String> paramMap) {
		return cm20Mapper.selectSanctnList(paramMap);
	}

	@Override
	public int selectSanctnCount(Map<String, String> paramMap) {
		return cm20Mapper.selectSanctnCount(paramMap);
	}

	@Override
	public Map<String, String> selectSanctnInfo(Map<String, String> paramMap) {
		return cm20Mapper.selectSanctnInfo(paramMap);
	}

	@Override
	public int deleteSanctnInfo(Map<String, String> paramMap) {
		return cm20Mapper.deleteSanctnInfo(paramMap);
	}

	@Override
	@SuppressWarnings("all")
	public int insertSanctnInfo(Map<String, Object> paramMap) {
		int result = 0;
		
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += cm20Mapper.insertSanctnInfo(detailMap);
		}
		return result;
	}

	@Override
	public int updateSanctnInfo(Map<String, String> paramMap) {
		return cm20Mapper.updateSanctnInfo(paramMap);
		
	}
}