package com.dksys.biz.user.sd.sd02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sd.sd02.mapper.SD02Mapper;
import com.dksys.biz.user.sd.sd02.service.SD02Svc;

@Service
public class SD02Svcmpl implements SD02Svc {
	
    @Autowired
    SD02Mapper sd02Mapper;
    
    @Override
	public int selectSellCount(Map<String, String> param) {
		return sd02Mapper.selectSellCount(param);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> param) {
		return sd02Mapper.selectSellList(param);
	}
	
	@Override
	public Map<String, String> selectPlanInfo(Map<String, String> paramMap) {
		return sd02Mapper.selectPlanInfo(paramMap);
	}
    
    @Override
	public int insertPlan(Map<String, String> param) {
		return sd02Mapper.insertPlan(param);
	}
    
    @Override
	public int updatePlan(Map<String, String> param) {
		return sd02Mapper.updatePlan(param);
	}

    @Override
    public int copyInsert(Map<String, String> param) {
    	return sd02Mapper.copyInsert(param);
    }
    
    @Override
    public int deleteCopy(Map<String, String> param) {
    	return sd02Mapper.deleteCopy(param);
    }
    
    @Override
	public int deletePlan(Map<String, String> paramMap) {
    	return sd02Mapper.deletePlan(paramMap);
		 
	}
}