package com.dksys.biz.user.ar.ar06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar06.mapper.AR06Mapper;
import com.dksys.biz.user.ar.ar06.service.AR06Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR06Svcmpl implements AR06Svc {
	
    @Autowired
    AR06Mapper ar06Mapper;

    @Override
	public int selectTaxInvoiceCount(Map<String, String> paramMap) {
		return ar06Mapper.selectTaxInvoiceCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxInvoiceList(Map<String, String> paramMap) {
		return ar06Mapper.selectTaxInvoiceList(paramMap);
	}
	
	@Override
	public Map<String, String> selectTaxInvoiceInfo(Map<String, String> paramMap) {
		return ar06Mapper.selectTaxInvoiceInfo(paramMap);
	}
	
	@Override
	public int insertTaxInvoice(Map<String, String> param) {
		return ar06Mapper.insertTaxInvoice(param);
	}

	@Override
	public int updateTaxInvoice(Map<String, String> param) {
		return ar06Mapper.updateTaxInvoice(param);
	}
    
	@Override
	public int deleteTaxInvoice(Map<String, String> param) {
		return ar06Mapper.deleteTaxInvoice(param);
	}
}