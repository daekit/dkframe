package com.dksys.biz.user.ar.ar13.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar13.service.AR13Svc;
import com.dksys.biz.user.od.od05.service.OD05Svc;
import com.dksys.biz.user.sd.sd05.mapper.SD05Mapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR13SvcImpl implements AR13Svc {
	
	@Autowired
	SD05Mapper sd05Mapper;
	
	@Override
	public int selectPrjtMngTernKeyCount(Map<String, String> paramMap) {
		return sd05Mapper.selectPrjtMngTernKeyCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPrjtMngTernKey(Map<String, String> paramMap) {
		return sd05Mapper.selectPrjtMngTernKey(paramMap);
	}
}