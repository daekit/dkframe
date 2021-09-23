package com.dksys.biz.user.ar.ar07.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar07.mapper.AR07Mapper;
import com.dksys.biz.user.ar.ar07.service.AR07Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR07SvcImpl implements AR07Svc {
	
    @Autowired
    AR07Mapper ar07Mapper;
    
	@Override
	public int selectMtClosCditCount(Map<String, String> paramMap) {
		return ar07Mapper.selectMtClosCditCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMtClosCditList(Map<String, String> paramMap) {
		return ar07Mapper.selectMtClosCditList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClosCditList(Map<String, String> paramMap) {
		
		List<Map<String, String>> detailList = ar07Mapper.selectClosCditList(paramMap);
		
//	//	for(Map<String, String> detailMap : detailList) {
//	for(int i = 0; i < detailList.size();i++){
//			Map<String, String> detailMap = detailList.get(i);
//			long curTrmendCreditAmt  = Long.parseLong(detailMap.get("curTrmendCreditAmt")); // 현재월 채권잔액			
//			
//			long curCellTotAmt  = Long.parseLong(detailMap.get("curCellTotAmt"));  // 현재월 매출
//			long pre1CellTotAmt = Long.parseLong(detailMap.get("pre1CellTotAmt")); // 1개월전 매출
//			long pre2CellTotAmt = Long.parseLong(detailMap.get("pre2CellTotAmt")); // 2개월전 매출
//			long pre1TrmendAmt  = Long.parseLong(detailMap.get("pre1TrmendAmt"));  // 1개월전 채권잔액( = 당월 기초)
//			long pre2TrmendAmt  = Long.parseLong(detailMap.get("pre2TrmendAmt"));  // 2개월전 채권잔액( = 1개월전 기초)
//			long pre3TrmendAmt  = Long.parseLong(detailMap.get("pre3TrmendAmt"));  // 3개월전 채권잔액( = 2개월전 기초)
//
//			long curCellClmnAmt  = Long.parseLong(detailMap.get("curCellClmnAmt"));  // 현재월 수금
//			long pre1CellClmnAmt = Long.parseLong(detailMap.get("pre1CellClmnAmt")); // 1개월전 수금
//			long pre2CellClmnAmt = Long.parseLong(detailMap.get("pre2CellClmnAmt")); // 2개월전 수금
//			
//			// 마감전 당월일 경우 현재 잔액이 없음으로 계산함.
//			if("Y".equals(paramMap.get("curDataYn"))) {
//				curTrmendCreditAmt = pre1TrmendAmt + curCellTotAmt - curCellClmnAmt;
//				detailMap.put("CUR_TRMEND_CREDIT_AMT",String.valueOf(curTrmendCreditAmt));
////				detailMap.put("CUR_TRMEND_CREDIT_AMT",String.valueOf(curTrmendCreditAmt));
//			}
//			
//			// 현재월잔액 - 현재월 매출  1. > 0 즉 잔액이 현재월매출 보다 많을 경우 잔액을 전월로 넘기고, 아니면 잔액을 전부 현재월 채권으로 계산
//			if( curTrmendCreditAmt - curCellTotAmt > 0) {
//				curTrmendCreditAmt = curTrmendCreditAmt - curCellTotAmt;
//				detailMap.put("CUR_CREDIT_AMT", String.valueOf(curCellTotAmt));
//			}else {
//				detailMap.put("CUR_CREDIT_AMT", String.valueOf(curTrmendCreditAmt));
//				curTrmendCreditAmt = 0;
//			}
//			// 잔액이 남았으면 1개월전 매출과 비교
//			if(curTrmendCreditAmt > 0) {
//				if(curTrmendCreditAmt - pre1CellTotAmt > 0  ) {
//					curTrmendCreditAmt = curTrmendCreditAmt - pre1CellTotAmt;
//					detailMap.put("PRE1_CREDIT_AMT", String.valueOf(pre1CellTotAmt));
//				}else {
//					detailMap.put("PRE1_CREDIT_AMT", String.valueOf(curTrmendCreditAmt));
//					curTrmendCreditAmt = 0;
//				}
//			}else {
//				detailMap.put("PRE1_CREDIT_AMT","0");
//			}
//			// 잔액이 남았으면 2개월전 매출과 비교
//			if(curTrmendCreditAmt > 0) {
//				if(curTrmendCreditAmt - pre2CellTotAmt > 0  ) {
//					curTrmendCreditAmt = curTrmendCreditAmt - pre2CellTotAmt;
//					detailMap.put("PRE2_CREDIT_AMT", String.valueOf(pre2CellTotAmt));
//				}else {
//					detailMap.put("PRE2_CREDIT_AMT", String.valueOf(curTrmendCreditAmt));
//					curTrmendCreditAmt = 0;
//				}
//			}else {
//				detailMap.put("PRE2_CREDIT_AMT","0");
//			}
//			// 잔액이 남았으면 3개월전 잔액으로 
//			if(curTrmendCreditAmt > 0) {
//				detailMap.put("PRE3_CREDIT_AMT", String.valueOf(curTrmendCreditAmt));
//			}else {
//				detailMap.put("PRE3_CREDIT_AMT","0");
//			}
//			
//		}
		
		
		return detailList;
	}

	@Override
	public int selectMtCloseChkCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectMtCloseChkCount(paramMap) ;
	}

	@Override
	public int selectMtClosCditPreCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectMtClosCditPreCount(paramMap) ; 
	}

	@Override
	public List<Map<String, String>> selectMtCloseCditPreList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectMtCloseCditPreList(paramMap) ; 
	}

	@Override
	public List<Map<String, String>> selectEtrdpsSellList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectEtrdpsSellList(paramMap) ; 
	}

	@Override
	public int selectPreMtCloseChkCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectPreMtCloseChkCount(paramMap) ; 
	}

	@Override
	public int selectPreMtClosCditCount(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return ar07Mapper.selectPreMtClosCditCount(paramMap) ; 
	}

	@Override
	public List<Map<String, String>> selectPreMtClosCditList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return  ar07Mapper.selectPreMtClosCditList(paramMap) ; 
	}
	
}