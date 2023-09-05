package com.dksys.biz.user.rd.rd05.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.rd.rd05.mapper.RD05Mapper;
import com.dksys.biz.user.rd.rd05.service.RD05Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class RD05SvcImpl implements RD05Svc {
	
    @Autowired
    RD05Mapper rd05Mapper;
    
	@Override
	public int selectRcvpayDailyCount(Map<String, String> paramMap) {
		return rd05Mapper.selectRcvpayDailyCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectRcvpayDailyList(Map<String, String> paramMap) {
		return rd05Mapper.selectRcvpayDailyList(paramMap);
	}
	
	@Override
	public int selectRcvpayDailyOnlyPrdtCount(Map<String, String> param) {
		return rd05Mapper.selectRcvpayDailyOnlyPrdtCount(param);
	}

	@Override
	public List<Map<String, String>> selectRcvpayDailyListOnlyPrdt(Map<String, String> param) {
		return rd05Mapper.selectRcvpayDailyListOnlyPrdt(param);
	}
    
	@Override
	public int selectRcvpayDailyDtlCount(Map<String, String> paramMap) {
		return rd05Mapper.selectRcvpayDailyDtlCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectRcvpayDailyDtlList(Map<String, String> paramMap) {
		return rd05Mapper.selectRcvpayDailyDtlList(paramMap);
	}


}