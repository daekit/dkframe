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
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.util.DateUtil;

@Service
@Transactional("erpTransactionManager")
public class AR05SvcImpl implements AR05Svc {
	
    @Autowired
    AR05Mapper ar05Mapper;
    
	@Autowired
	SD07Mapper sd07Mapper;
	
    
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
	public int insertEtrdps(Map<String, Object> paramMap) {
		Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
		Map<String, String> billData = (Map<String, String>) paramMap.get("billData");

		int result = 0;
		
		//마감 체크
		if(checkEtrdpsClose(etrdpsData)) {
			return 500;				
		}
		
		// 수금 등록
		if(billData != null) {
			// 결제방법이 어음일때 bilNo put
			etrdpsData.put("bilNo", billData.get("bilNo"));
			
			// 결제방법이 어음일때 clntCd put
			billData.put("clntCd", etrdpsData.get("clntCd"));
			ar05Mapper.insertBill(billData);
		}
		result = ar05Mapper.insertEtrdps(etrdpsData);
		
		// 여신 증가
		Map<String, Object> cdtlnData = new HashMap<String, Object>();
		cdtlnData.putAll(etrdpsData);
		int clntCd = Integer.parseInt(etrdpsData.get("clntCd"));
		long etrdpsAmt = Long.parseLong(etrdpsData.get("etrdpsAmt"));
		cdtlnData.put("clntCd", clntCd);
		cdtlnData.put("etrdpsAmt", etrdpsAmt);
		ar05Mapper.callCreditLoan(cdtlnData);
		

		return result;
		
	}
	
	@Override
	public int updateEtrdps(Map<String, Object> paramMap) {
		Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
		Map<String, String> billData = (Map<String, String>) paramMap.get("billData");

		int result = 0;
		//마감 체크
		if(checkEtrdpsClose(etrdpsData)) {
			return 500;				
		}
		
		if(billData != null) {
			// 결제방법이 어음일때
			ar05Mapper.updateBill(billData);
		}
		result = ar05Mapper.updateEtrdps(etrdpsData);
		return result;
	}

	@Override
	public int deleteEtrdps(Map<String, String> paramMap) {
		Map<String, String> etrdpsInfo = ar05Mapper.selectEtrdpsInfo(paramMap);
		int result = 0;
		//마감 체크
		paramMap.put("coCd", etrdpsInfo.get("coCd"));
		paramMap.put("etrdpsDt", etrdpsInfo.get("etrdpsDt"));
		if(checkEtrdpsClose(paramMap)) {
			return 500;				
		}
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
		result = ar05Mapper.deleteEtrdps(paramMap);
		return result;
	}
	
	@Override
	public boolean checkEtrdpsClose(Map<String, String> paramMap) {
		/*
		 * 1. 기준월에 대한 수정 가능 여부 확인 
		 * 2. 수정하고자 하는 자료의 일자가 마감이 된 MAX월 이전인지 확인 
		 */
		String etrdpsDt = paramMap.get("etrdpsDt").replace("-", "");
		paramMap.put("closeYm", etrdpsDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		Map<String, String> sd07resultMax = sd07Mapper.selectMaxCloseDay(paramMap);
		if(sd07result != null) {
			int today       = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay    = Integer.parseInt(sd07result.get("etrdpsCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("etrdpsDtCloseYn")) && today > closeDay) {
				return true;
			}
		}
		// 마지막 마감일체츠하여 그 이전이면 수정불가
		int maxEtrdpsCloseDay = Integer.parseInt(sd07resultMax.get("maxEtrdpsCloseDay").replace("-", ""));
		if(sd07resultMax != null && maxEtrdpsCloseDay > Integer.parseInt(etrdpsDt)) {
			return true;
		}
		return false;
	}
}