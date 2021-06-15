package com.dksys.biz.admin.cm.cm05.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm05.mapper.CM05Mapper;
import com.dksys.biz.admin.cm.cm05.service.CM05Svc;

@Service
public class CM05SvcImpl implements CM05Svc {
	
    @Autowired
    CM05Mapper cm05Mapper;
    
    @Override
	public int selectCodeCount(Map<String, String> param) {
		return cm05Mapper.selectCodeCount(param);
	}
    
	@Override
	public List<Map<String, String>> selectCodeList(Map<String, String> param) {
		return cm05Mapper.selectCodeList(param);
	}
	
    @Override
	public int selectPdskCodeCount(Map<String, String> param) {
		return cm05Mapper.selectPdskCodeCount(param);
	}
    
	@Override
	public List<Map<String, String>> selectPdskCodeList(Map<String, String> param) {
		return cm05Mapper.selectPdskCodeList(param);
	}
	
	@Override
	public List<Map<String, String>> selectChildCodeList(Map<String, String> param) {
		return cm05Mapper.selectChildCodeList(param);
	}
	
	@Override
	public Map<String, String> selectCodeInfo(Map<String, String> param) {
		return cm05Mapper.selectCodeInfo(param);
	}
	
	@Override
	public List<Map<String, String>> selectCodeInfoList(Map<String, String> param) {
		return cm05Mapper.selectCodeInfoList(param);
	}
	
	@Override
	public int insertCode(Map<String, String> param) throws Exception {
		return cm05Mapper.insertCode(param);
	}

	@Override
	public int deleteCode(Map<String, String> param) {
		return cm05Mapper.deleteCode(param);
	}

}