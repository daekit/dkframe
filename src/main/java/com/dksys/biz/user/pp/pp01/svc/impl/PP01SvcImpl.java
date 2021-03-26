package com.dksys.biz.user.pp.pp01.svc.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.pp.pp01.mapper.PP01Mapper;
import com.dksys.biz.user.pp.pp01.svc.PP01Svc;

@Service
@Transactional("erpTransactionManager")
public class PP01SvcImpl implements PP01Svc {
	
    @Autowired
    PP01Mapper pp01Mapper;
    
	@Override
	public int selectMesOrderCount(Map<String, String> paramMap) {
		return pp01Mapper.selectMesOrderCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap) {
		return pp01Mapper.selectMesOrderList(paramMap);
	}
	
}