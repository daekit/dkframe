package com.dksys.biz.admin.cm.cm03.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm03.mapper.CM03Mapper;
import com.dksys.biz.admin.cm.cm03.service.CM03Svc;

@Service
public class CM03SvcImpl implements CM03Svc {
	
    @Autowired
    CM03Mapper cm03Mapper;

	@Override
	public List<Map<String, String>> selectMenuList() {
		return cm03Mapper.selectMenuList();
	}

	@Override
	public int insertMenu(Map<String, String> param) {
		return cm03Mapper.insertMenu(param);
	}

	@Override
	public int deleteMenu(Map<String, String> param) {
		return cm03Mapper.deleteMenu(param);
	}

	@Override
	public int updateMenu(Map<String, String> param) {
		return cm03Mapper.updateMenu(param);
	}

	@Override
	public int selectMenuCount(Map<String, String> param) {
		return cm03Mapper.selectMenuCount(param);
	}

}