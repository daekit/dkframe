package com.dksys.biz.user.ar.ar16.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar16.mapper.AR16Mapper;
import com.dksys.biz.user.ar.ar16.service.AR16Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR16SvcImpl implements AR16Svc {
	
	@Autowired
	AR16Mapper ar16Mapper;

	@Override
	public int selectLoanAmtCount(Map<String, String> paramMap) {
		return ar16Mapper.selectLoanAmtCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectLoanAmtList(Map<String, String> paramMap) {
		
		return ar16Mapper.selectLoanAmtList(paramMap);
	}

}
