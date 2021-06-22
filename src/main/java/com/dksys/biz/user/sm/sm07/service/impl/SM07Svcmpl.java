package com.dksys.biz.user.sm.sm07.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm07.mapper.SM07Mapper;
import com.dksys.biz.user.sm.sm07.service.SM07Svc;

@Service
@Transactional("erpTransactionManager")
public class SM07Svcmpl implements SM07Svc {
	
    @Autowired
    SM07Mapper sm07Mapper;

	@Override
	public int selectStockSummaryListCount(Map<String, String> param) {
		return sm07Mapper.selectStockSummaryListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockSummaryList(Map<String, String> param) {
		return sm07Mapper.selectStockSummaryList(param);
	}
	
	@Override
	public int selectStockSummaryDetailListCount(Map<String, String> param) {
		return sm07Mapper.selectStockSummaryDetailListCount(param);
	}
	
	@Override
	public List<Map<String, String>> selectStockSummaryDetailList(Map<String, String> param) {
		return sm07Mapper.selectStockSummaryDetailList(param);
	}
	
	@Override
	public String selectSearchDttm() {
		return sm07Mapper.selectSearchDttm();
	}
    
}