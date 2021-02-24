package com.dksys.biz.user.sd.sd01.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sd.sd01.mapper.SD01Mapper;
import com.dksys.biz.user.sd.sd01.service.SD01Svc;

@Service
public class SD01Svcmpl implements SD01Svc {
	
    @Autowired
    SD01Mapper sd01Mapper;
    
    @Override
	public int selectSellCount(Map<String, String> param) {
		return sd01Mapper.selectSellCount(param);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> param) {
		return sd01Mapper.selectSellList(param);
	}
	
	@Override
	public Map<String, String> selectPlanInfo(Map<String, String> paramMap) {
		return sd01Mapper.selectPlanInfo(paramMap);
	}
    
    @Override
	public int insertPlan(Map<String, String> param) {
		return sd01Mapper.insertPlan(param);
	}
    
    @Override
	public int updatePlan(Map<String, String> param) {
		return sd01Mapper.updatePlan(param);
	}

    @Override
    public int copyInsert(Map<String, String> param) {
    	return sd01Mapper.copyInsert(param);
    }
    @Override
    public int deleteCopy(Map<String, String> param) {
    	return sd01Mapper.deleteCopy(param);
    }
}