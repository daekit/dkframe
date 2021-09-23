package com.dksys.biz.user.ar.ar12.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar12.mapper.AR12Mapper;
import com.dksys.biz.user.ar.ar12.service.AR12Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR12Svcmpl implements AR12Svc {
	
    @Autowired
    AR12Mapper ar12Mapper;
    
    @Override
	public int SelectPtnCreditCount(Map<String, String> paramMap) {
    	return ar12Mapper.SelectPtnCreditCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> SelectPtnCreditList(Map<String, String> paramMap) {
		return ar12Mapper.SelectPtnCreditList(paramMap);
	}
}