package com.dksys.biz.user.rd.rd04.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.rd.rd04.mapper.RD04Mapper;
import com.dksys.biz.user.rd.rd04.service.RD04Svc;

@Service
@Transactional("erpTransactionManager")
public class RD04SvcImpl implements RD04Svc {
	
    @Autowired
    RD04Mapper rd04Mapper;
    
	@Override
	public int selectRcvpayCount(Map<String, String> paramMap) {
		return rd04Mapper.selectRcvpayCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectRcvpayList(Map<String, String> paramMap) {
		return rd04Mapper.selectRcvpayList(paramMap);
	}
}