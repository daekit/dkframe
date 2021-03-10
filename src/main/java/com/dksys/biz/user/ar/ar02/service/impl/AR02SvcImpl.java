package com.dksys.biz.user.ar.ar02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;

@Service
@Transactional("erpTransactionManager")
public class AR02SvcImpl implements AR02Svc {

	@Autowired
    AR02Mapper ar02Mapper;
	
	@Override
	public int selectSellCount(Map<String, String> paramMap) {
		return ar02Mapper.selectSellCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellList(paramMap);
	}

	@Override
	public int updatePchsSell(Map<String, String> paramMap) {
		return ar02Mapper.updatePchsSell(paramMap);
	}
	
}