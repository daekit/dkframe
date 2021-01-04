package com.dksys.biz.admin.itm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.itm.mapper.ItmMapper;
import com.dksys.biz.admin.itm.service.ItmService;

@Service
@Transactional
public class ItmServiceImpl implements ItmService {
	
    @Autowired
    ItmMapper itmMapper;

	@Override
	public List<Map<String, String>> selectItmList() {
		return itmMapper.selectItmList();
	}

	@Override
	public int insertItm(Map<String, String> param) {
		return itmMapper.insertItm(param);
	}

	@Override
	public int deleteItm(Map<String, String> param) {
		return itmMapper.deleteItm(param);
	}

	@Override
	public int updateItm(Map<String, String> param) {
		return itmMapper.updateItm(param);
	}

	@Override
	public Map<String, String> selectItmInfo(Map<String, String> paramMap) {
		return itmMapper.selectItmInfo(paramMap);
	}
}