package com.dksys.biz.user.sm.sm08.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm08.mapper.SM08Mapper;
import com.dksys.biz.user.sm.sm08.service.SM08Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class SM08Svcmpl implements SM08Svc {
	
    @Autowired
    SM08Mapper sm08Mapper;

	@Override
	public int selectRcvpayTnKeyDailyListCount(Map<String, String> param) {
		return sm08Mapper.selectRcvpayTnKeyDailyListCount(param);
	}

	@Override
	public List<Map<String, String>> selectRcvpayTnKeyDailyList(Map<String, String> param) {
		return sm08Mapper.selectRcvpayTnKeyDailyList(param);
	}
    
}