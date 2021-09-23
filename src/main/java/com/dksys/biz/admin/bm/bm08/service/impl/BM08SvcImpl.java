package com.dksys.biz.admin.bm.bm08.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm08.mapper.BM08Mapper;
import com.dksys.biz.admin.bm.bm08.service.BM08Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class BM08SvcImpl implements BM08Svc {
	
    @Autowired
    BM08Mapper bm08Mapper;

	@Override
	public int selectClntPledgeCount(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPledgeCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntPledgeList(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPledgeList(paramMap);
	}

	@Override
	public int selectClntPledgeDetailCount(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPledgeDetailCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectClntPledgeDetailList(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPledgeDetailList(paramMap);
	}

	@Override
	public int selectClntPldgHistoryCount(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPldgHistoryCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntPldgHistoryList(Map<String, String> paramMap) {
		return bm08Mapper.selectClntPldgHistoryList(paramMap);
	}

	
}