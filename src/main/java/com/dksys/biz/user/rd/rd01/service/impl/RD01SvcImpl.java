package com.dksys.biz.user.rd.rd01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.rd.rd01.mapper.RD01Mapper;
import com.dksys.biz.user.rd.rd01.service.RD01Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class RD01SvcImpl implements RD01Svc {
	
    @Autowired
    RD01Mapper rd01Mapper;

	@Override
	public void inOutPrdt(List<Map<String, String>> paramList) {
		for(Map<String, String> paramMap : paramList) {
			rd01Mapper.inOutPrdt(paramMap);
		}
	}
}