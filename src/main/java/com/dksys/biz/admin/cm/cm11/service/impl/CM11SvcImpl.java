package com.dksys.biz.admin.cm.cm11.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm11.mapper.CM11Mapper;
import com.dksys.biz.admin.cm.cm11.service.CM11Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class CM11SvcImpl implements CM11Svc {
	
    @Autowired
    CM11Mapper cm11Mapper;
    
	@Override
	public String selectSearchDttm() {
		return cm11Mapper.selectSearchDttm();
	}
    
	@Override
	public int selectPrdtSelpch2Count(Map<String, String> paramMap) {
		return cm11Mapper.selectPrdtSelpch2Count(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPrdtSelpch2List(Map<String, String> paramMap) {
		return cm11Mapper.selectPrdtSelpch2List(paramMap);
	}
    
	@Override
	public int selectClntSelpch2Count(Map<String, String> paramMap) {
		return cm11Mapper.selectClntSelpch2Count(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntSelpch2List(Map<String, String> paramMap) {
		return cm11Mapper.selectClntSelpch2List(paramMap);
	}
    
	@Override
	public int selectClntSelpch1Count(Map<String, String> paramMap) {
		return cm11Mapper.selectClntSelpch1Count(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntSelpch1List(Map<String, String> paramMap) {
		return cm11Mapper.selectClntSelpch1List(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntSelpch1List2(Map<String, String> paramMap) {
		return cm11Mapper.selectClntSelpch1List2(paramMap);
	}

	@Override
	public List<Map<String, String>> selectFacList(Map<String, String> paramMap) {
		return cm11Mapper.selectFacList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMonthlyStat(Map<String, String> paramMap) {
		return cm11Mapper.selectMonthlyStat(paramMap);
	}
}