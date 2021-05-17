package com.dksys.biz.user.sm.sm05.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm05.mapper.SM05Mapper;
import com.dksys.biz.user.sm.sm05.service.SM05Svc;

@Service
@Transactional("erpTransactionManager")
public class SM05Svcmpl implements SM05Svc {
	
    @Autowired
    SM05Mapper sm05Mapper;

    @Override
	public int selectPchsAggrCount(Map<String, String> paramMap) {
		return sm05Mapper.selectPchsAggrCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPchsAggrList(Map<String, String> paramMap) {
		return sm05Mapper.selectPchsAggrList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> bilgCertNoList(Map<String, String> paramMap) {
		return sm05Mapper.bilgCertNoList(paramMap);
	}

	@Override
	public List<Map<String, String>> bilgCertNullNoList(Map<String, String> paramMap) {
		return sm05Mapper.bilgCertNullNoList(paramMap);
	}
	
}