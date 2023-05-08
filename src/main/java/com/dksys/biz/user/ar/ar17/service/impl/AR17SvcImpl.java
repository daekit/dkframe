package com.dksys.biz.user.ar.ar17.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar16.mapper.AR16Mapper;
import com.dksys.biz.user.ar.ar16.service.AR16Svc;
import com.dksys.biz.user.ar.ar17.mapper.AR17Mapper;
import com.dksys.biz.user.ar.ar17.service.AR17Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR17SvcImpl implements AR17Svc {
	
	@Autowired
	AR17Mapper ar17Mapper;

	@Override
	public int selectTrmendAmtListCount(Map<String, String> paramMap) {
		return ar17Mapper.selectTrmendAmtListCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTrmendAmtList(Map<String, String> paramMap) {
		return ar17Mapper.selectTrmendAmtList(paramMap);
	}

}
