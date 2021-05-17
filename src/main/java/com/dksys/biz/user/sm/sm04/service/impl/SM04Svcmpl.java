package com.dksys.biz.user.sm.sm04.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm04.mapper.SM04Mapper;
import com.dksys.biz.user.sm.sm04.service.SM04Svc;

@Service
@Transactional("erpTransactionManager")
public class SM04Svcmpl implements SM04Svc {
	
    @Autowired
    SM04Mapper sm04Mapper;

    @Override
	public int selectPrdtAcinsCount(Map<String, String> paramMap) {
		return sm04Mapper.selectPrdtAcinsCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPrdtAcinsList(Map<String, String> paramMap) {
		return sm04Mapper.selectPrdtAcinsList(paramMap);
	}
	
	@Override
	public Map<String, String> selectPrdtAcinsInfo(Map<String, String> paramMap) {
		return sm04Mapper.selectPrdtAcinsInfo(paramMap);
	}
	
	@Override
	public int insertPrdtAcins(Map<String, String> param) {
		return sm04Mapper.insertPrdtAcins(param);
	}

	@Override
	public int updatePrdtAcins(Map<String, String> param) {
		return sm04Mapper.updatePrdtAcins(param);
	}
	
	@Override
    public int copyInsert(Map<String, String> param) {
    	return sm04Mapper.copyInsert(param);
    }
    
	@Override
	public int deletePrdtAcins(Map<String, String> param) {
		return sm04Mapper.deletePrdtAcins(param);
	}
	
    @Override
    public int deleteCopy(Map<String, String> param) {
    	return sm04Mapper.deleteCopy(param);
    }
}