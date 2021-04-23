package com.dksys.biz.user.ar.ar08.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.ar.ar08.mapper.AR08Mapper;
import com.dksys.biz.user.ar.ar08.service.AR08Svc;

@Service
public class AR08Svcmpl implements AR08Svc {
	
    @Autowired
    AR08Mapper ar08Mapper;

    @Override
	public int selectCreditCount(Map<String, String> paramMap) {
		return ar08Mapper.selectCreditCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCreditList(Map<String, String> paramMap) {
		return ar08Mapper.selectCreditList(paramMap);
	}

}