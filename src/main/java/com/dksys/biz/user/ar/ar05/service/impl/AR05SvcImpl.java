package com.dksys.biz.user.ar.ar05.service.impl;

import java.text.SimpleDateFormat;
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
		
		// 수금 등록
		if(billData != null) {
			// 결제방법이 어음일때 bilNo put
			etrdpsData.put("bilNo", billData.get("bilNo"));
			
			// 결제방법이 어음일때 clntCd put
			billData.put("clntCd", etrdpsData.get("clntCd"));
			ar05Mapper.insertBill(billData);
		}
		ar05Mapper.insertEtrdps(etrdpsData);
		
		// 여신 증가
		Map<String, Object> cdtlnData = new HashMap<String, Object>();
		cdtlnData.putAll(etrdpsData);
		int clntCd = Integer.parseInt(etrdpsData.get("clntCd"));
		int etrdpsAmt = Integer.parseInt(etrdpsData.get("etrdpsAmt"));
		cdtlnData.put("clntCd", clntCd);
		cdtlnData.put("etrdpsAmt", etrdpsAmt);
		ar05Mapper.callCreditLoan(cdtlnData);
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

	@Override
	public void deleteEtrdps(Map<String, String> paramMap) {
		Map<String, String> etrdpsInfo = ar05Mapper.selectEtrdpsInfo(paramMap);
		
		// 여신 감소
		Map<String, Object> cdtlnData = new HashMap<String, Object>();
		cdtlnData.putAll(etrdpsInfo);
		int clntCd = Integer.parseInt(etrdpsInfo.get("clntCd"));
		int etrdpsAmt = Integer.parseInt(etrdpsInfo.get("etrdpsAmt"));
		String etrdpsDt = etrdpsInfo.get("etrdpsDt").replaceAll("-", "");
		
		cdtlnData.put("clntCd", clntCd);
		cdtlnData.put("etrdpsAmt", -1 * etrdpsAmt);
		cdtlnData.put("etrdpsDt", etrdpsDt);
		ar05Mapper.callCreditLoan(cdtlnData);
		
		// 수금 삭제
		if(etrdpsInfo.get("bilNo") != null) {
			// 결제방법이 어음일때 어음 삭제
			paramMap.put("bilNo", etrdpsInfo.get("bilNo"));
			ar05Mapper.deleteBill(paramMap);
		}
		ar05Mapper.deleteEtrdps(paramMap);
	}
}