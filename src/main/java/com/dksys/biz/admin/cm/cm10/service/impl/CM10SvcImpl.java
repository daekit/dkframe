package com.dksys.biz.admin.cm.cm10.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm10.mapper.CM10Mapper;
import com.dksys.biz.admin.cm.cm10.service.CM10Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class CM10SvcImpl implements CM10Svc {
	
    @Autowired
    CM10Mapper cm10Mapper;
    
	@Override
	public int selectShipNCount(Map<String, String> paramMap) {
		return cm10Mapper.selectShipNCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectShipNList(Map<String, String> paramMap) {
		return cm10Mapper.selectShipNList(paramMap);
	}
    
	@Override
	public int selectOrdrgNCount(Map<String, String> paramMap) {
		return cm10Mapper.selectOrdrgNCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectOrdrgNList(Map<String, String> paramMap) {
		return cm10Mapper.selectOrdrgNList(paramMap);
	}

	@Override
	public int selectReqNCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return cm10Mapper.selectReqNCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectReqNList(Map<String, String> paramMap) {
		return cm10Mapper.selectReqNList(paramMap);
	}
	
	@Override
	public int selectTaxNCount(Map<String, String> paramMap) {
		return cm10Mapper.selectTaxNCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectTaxNList(Map<String, String> paramMap) {
		return cm10Mapper.selectTaxNList(paramMap);
	}
}