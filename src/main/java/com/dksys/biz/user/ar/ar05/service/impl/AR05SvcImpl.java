package com.dksys.biz.user.ar.ar05.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar05.mapper.AR05Mapper;
import com.dksys.biz.user.ar.ar05.service.AR05Svc;

@Service
@Transactional("erpTransactionManager")
public class AR05SvcImpl implements AR05Svc {
	
    @Autowired
    AR05Mapper ar05Mapper;
    
	@Override
	public int selectEtrdpsCount(Map<String, String> paramMap) {
		return ar05Mapper.selectEtrdpsCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectEtrdpsList(Map<String, String> paramMap) {
		return ar05Mapper.selectEtrdpsList(paramMap);
	}

	@Override
	public Map<String, Object> selectEtrdpsInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		Map<String, String> etrdpsData = ar05Mapper.selectEtrdpsInfo(paramMap);
		returnMap.put("etrdpsData", etrdpsData);
		
		String bilNo = etrdpsData.get("bilNo");
		if(bilNo != null && !"".equals(bilNo)) {
			paramMap.put("bilNo", bilNo);
			Map<String, String> billData = ar05Mapper.selectBillInfo(paramMap);
			returnMap.put("billData", billData);
		}
		
		return returnMap;
	}
	
	@Override
	public void insertEtrdps(Map<String, Object> paramMap) {
		Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
		Map<String, String> billData = (Map<String, String>) paramMap.get("billData");
		if(billData != null) {
			// 결제방법이 어음일때 bilNo put
			etrdpsData.put("bilNo", billData.get("bilNo"));
			
			// 결제방법이 어음일때 clntCd put
			billData.put("clntCd", etrdpsData.get("clntCd"));
			ar05Mapper.insertBill(billData);
		}
		ar05Mapper.insertEtrdps(etrdpsData);
	}
	
	@Override
	public void updateEtrdps(Map<String, Object> paramMap) {
		Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
		Map<String, String> billData = (Map<String, String>) paramMap.get("billData");
		if(billData != null) {
			// 결제방법이 어음일때
			ar05Mapper.updateBill(billData);
		}
		ar05Mapper.updateEtrdps(etrdpsData);
	}

	// 삭제 구현하고 <String, Object> 고찰
	@Override
	public void deleteEtrdps(Map<String, String> paramMap) {
		Map<String, String> etrdpsInfo = ar05Mapper.selectEtrdpsInfo(paramMap);
		if(etrdpsInfo.get("bilNo") != null) {
			// 결제방법이 어음일때 어음 삭제
			paramMap.put("bilNo", etrdpsInfo.get("bilNo"));
			ar05Mapper.deleteBill(paramMap);
		}
		ar05Mapper.deleteEtrdps(paramMap);
	}
}