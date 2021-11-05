package com.dksys.biz.admin.bm.bm09.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm09.mapper.BM09Mapper;
import com.dksys.biz.admin.bm.bm09.service.BM09Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class BM09SvcImpl implements BM09Svc {
	
    @Autowired
    BM09Mapper bm09Mapper;

	@Override
	public int selectPledgeCount(Map<String, String> paramMap) {
		return bm09Mapper.selectPledgeCount(paramMap);
	}

	@Override 
	public List<Map<String, String>> selectPledgeList(Map<String, String> paramMap) {
		return bm09Mapper.selectPledgeList(paramMap);
	}

	@Override
	public int selectPledgeDetailCount(Map<String, String> paramMap) {
		return bm09Mapper.selectPledgeDetailCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectPledgeDetailList(Map<String, String> paramMap) {
		return bm09Mapper.selectPledgeDetailList(paramMap);
	}

	@Override
	public int selectPldgHistoryCount(Map<String, String> paramMap) {
		return bm09Mapper.selectPldgHistoryCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPldgHistoryList(Map<String, String> paramMap) {
		return bm09Mapper.selectPldgHistoryList(paramMap);
	}

	
}