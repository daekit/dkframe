package com.dksys.biz.admin.cm.cm09.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm09.mapper.CM09Mapper;
import com.dksys.biz.admin.cm.cm09.service.CM09Svc;

@Service
public class CM09SvcImpl implements CM09Svc {
	
    @Autowired
    CM09Mapper cm09Mapper;


	@Override
	public int insertNoti(Map<String, String> paramMap) {
		return cm09Mapper.insertNoti(paramMap);
	}

	@Override
	public List<Map<String, String>> selectNotiList(Map<String, String> paramMap) {
		return cm09Mapper.selectNotiList(paramMap);
	}

	@Override
	public int selectNotiCount(Map<String, String> paramMap) {
		return cm09Mapper.selectNotiCount(paramMap);
	}

}