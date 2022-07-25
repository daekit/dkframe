package com.dksys.biz.user.ar.ar05.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar05.mapper.AR05Mapper;
import com.dksys.biz.user.ar.ar05.service.AR05Svc;
import com.dksys.biz.user.fi.douzone.mapper.DouzoneMapper;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.media.sound.EmergencySoundbank;

@Service
@Transactional(rollbackFor = Exception.class )
public class AR05SvcImpl implements AR05Svc {
	
    @Autowired
    AR05Mapper ar05Mapper;
    
	@Autowired
	SD07Mapper sd07Mapper;
	
    @Autowired
    DouzoneMapper douzoneMapper;
    
	@Autowired
	BM02Mapper bm02Mapper;
    
	@Autowired
	AR02Mapper ar02Mapper;
    
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
		
		List<Map<String, String>> etrdpsList = ar05Mapper.selectEtrdpsDtlUpdate(paramMap);
		returnMap.put("etrdpsList", etrdpsList);
		return returnMap;
	}

	@Override
	public List<Map<String, String>> selectEtrdpsDtlList(Map<String, String> paramMap) {
		return ar05Mapper.selectEtrdpsDtlList(paramMap);
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
			
			Map<String, String> billMap = ar05Mapper.selectBillInfo(billData);
			if(billMap != null) {
				return 300;
			}
			
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
/*------------------------------------------------------------------------------*/		
		// 더존 I/F 시작
		Map<String, Object> douzoneParam = new HashMap<String, Object>();
		Map<String, Object> clntData = bm02Mapper.selectClntInfo(etrdpsData);
// 차변정리		
		douzoneParam.put("CO_CD","");   //	회사코드	
		douzoneParam.put("IN_DT", cdtlnData.get("etrdpsDt")); //거래일자	
		douzoneParam.put("IN_SQ","");  //	거래순번	
		douzoneParam.put("LN_SQ","");   //	분개라인순번	
		douzoneParam.put("ISU_DT","00000000"); //	결의일자	더존에서 사용
		douzoneParam.put("ISU_SQ","0");   //	결의번호	더존에서 사용
		douzoneParam.put("DIV_CD","");   //	회계단위	
		douzoneParam.put("DEPT_CD","");  //	결의부서	더존에서 사용
		douzoneParam.put("EMP_CD","");   //	작성자	더존에서 사용
		
		douzoneParam.put("DRCR_FG","3");   //	차대구분	3.차변 4.대변
		douzoneParam.put("ACCT_CD","11000");   //	계정코드	계정코드VIEW 참조
		douzoneParam.put("REG_NB","");   //	거래처 사업자번호	거래처등록 VIEW 참조
		douzoneParam.put("ACCT_AM",etrdpsData.get("etrdpsAmt")); //	금액	
		douzoneParam.put("RMK_DC", etrdpsData.get("sumry"));  //	적요	
		douzoneParam.put("RMK_DCK","");  //	적요(보조어)	
		douzoneParam.put("CCODE_TY","C1"); //	C관리항목 타입	"C1.사용부서만 지원 함.		계정과목등록 VIEW의 DEPTCD_TY 값을		조회하여 넣습니다."
		douzoneParam.put("CT_DEPT","");	 // 사용부서코드	사용부서코드
		douzoneParam.put("DCODE_TY","D1"); //	D관리항목 타입	"D1.프로젝트, D4.사원, D5사업장만 지원 함	계정과목등록VIEW의 PJTCD_TY 값을	조회하여 넣습니다."
		douzoneParam.put("PJT_CD", "");  //	프로젝트코드	"프로젝트 VIEW,	사원 VIEW,	사업장 VIEW 참조"
		douzoneParam.put("CT_AM",  etrdpsData.get("etrdpsAmt"));  //공급가액
		
		douzoneParam.put("CT_DEAL",  "");//	세무구분	"-부가세계정일 경우 : 세무구분코드 관리내역등록 VIEW 참조	-받을어음 계정일 경우 : '1' 로 등록		-지급어음 계정일 경우 : '2' 로 등록"
		if(billData != null) {
			if("ETRDPS01".equals(etrdpsData.get("etrdpsTyp"))) {
				douzoneParam.put("CT_DEAL",  "1");
			}else {
				douzoneParam.put("CT_DEAL",  "2");				
			}
		}
		douzoneParam.put("NONSUB_TY",""); //	사유구분	"-세무구분이 23.면세매입, 24.매입불공제		  26.의제매입일 경우 필수입력		  관리내역등록 VIEW참조"
		douzoneParam.put("FR_DT",""); //	시작일	"-부가세계정일 경우 : 신고기준일		-어음계정일 경우 : 발행일"
		douzoneParam.put("TO_DT", "");//	종료일	어음계정일 경우 : 만기일
		if(billData != null) {
			douzoneParam.put("FR_DT",billData.get("bilPblsDt"));
			douzoneParam.put("TO_DT",billData.get("exprntDt")); //	종료일	어음계정일 경우 : 만기일
		}
		douzoneParam.put("TO_DT", "");//	종료일	어음계정일 경우 : 만기일
		douzoneParam.put("ISU_DOC", "");//	품의내역	
		douzoneParam.put("ISU_DOCK", "");//	품의내역(보조어)	
		douzoneParam.put("JEONJA_YN",""); //	전자세금계산서여부	0.부 1.여
		douzoneParam.put("CT_NB", ""); //	관리번호	"-부가세계정이고 세무구분이 12.영세 16.수출일 경우 : 수출신고번호 -어음계정일 경우 : 어음번호"
		if(billData != null) {
		douzoneParam.put("CT_NB", etrdpsData.get("bilNo"));
		}		
		douzoneParam.put("CT_QT", "");//	환율	12.영세 16.수출일 경우에만 등록
		douzoneParam.put("DUMMY1", "");//	환종	12.영세 16.수출일 경우에만 등록
		douzoneParam.put("DUMMY2","");//	외화금액	12.영세 16.수출일 경우에만 등록
		
		douzoneParam.put("EMPTY1","");      //	여유1	
		douzoneParam.put("INSERT_ID","");   //	입력자ID	
	//  douzoneParam.put("INSERT_DT","");   //		입력일	
		douzoneParam.put("INSERT_IP","");   //	입력자IP	
	//	douzoneParam.put("MODIFY_ID", "");  //  수정자ID	전표발행 시 더존에서 UPDATE
	//	douzoneParam.put("MODIFY_DT","")	//  수정일	전표발행 시 더존에서 UPDATE
	//	douzoneParam.put("MODIFY_IP","");   //  수정자IP	전표발행 시 더존에서 UPDATE
		douzoneParam.put("CEO_NM",   clntData.get("repstNm"));  //	대표자명	
		douzoneParam.put("TR_NM",    clntData.get("clntNm"));   //	거래처명	
		douzoneParam.put("TR_NMK",""); //	거래처명(보조어)	
		douzoneParam.put("BUSINESS", clntData.get("bizconNm")); //	업태	
		douzoneParam.put("JONGMOK",  clntData.get("bstyNm")); //	종목	
		douzoneParam.put("ADDR1",    clntData.get("bizAddr")); //	주소	
		douzoneParam.put("ADDR2",    clntData.get("bizAddrDtl")); //	주소상세	
		douzoneParam.put("TR_CD",    clntData.get("crn"));//	거래처코드	"ICUBE 거래처코드를 알고 있을		경우 등록 (거래처등록 VIEW참조)		모를 경우 REG_NB 를 등록"
		douzoneParam.put("CT_USER1",""); //	사용자정의관리항목	L코드 관리항목 코드를 등록
		douzoneParam.put("CT_USER2",""); //	사용자정의관리항목	M코드 관리항목 코드를 등록

		//douzoneMapper.insertAutodocuSimple(douzoneParam);
//  대변 정리.
		douzoneParam.put("DRCR_FG","4");   //	차대구분	3.차변 4.대변
		douzoneParam.put("ACCT_CD","40101");   //	계정코드	계정코드VIEW 참조
		douzoneParam.put("FR_DT","");    //	시작일	"-부가세계정일 경우 : 신고기준일		-어음계정일 경우 : 발행일"
		douzoneParam.put("TO_DT", "");   //	종료일	어음계정일 경우 : 만기일
		douzoneParam.put("CT_DEPT","");	 // 사용부서코드	사용부서코드
		douzoneParam.put("PJT_CD", "");  //	프로젝트코드	"프로젝트 VIEW,	사원 VIEW,	사업장 VIEW 참조"
		douzoneParam.put("CT_NB", "");   //	관리번호	"-부가세계정이고 세무구분이 12.영세 16.수출일 경우 : 수출신고번호 -어음계정일 경우 : 어음번호"

		//douzoneMapper.insertAutodocuSimple(douzoneParam);

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gson.fromJson((String)paramMap.get("detailArr"), dtlMap);

		for(Map<String, String> dtl : dtlParam) {
			dtl.put("userId",  etrdpsData.get("userId"));
			dtl.put("pgmId",   etrdpsData.get("pgmId"));
			dtl.put("etrdpsSeq", etrdpsData.get("etrdpsSeq"));
			dtl.put("etrdpsDt", etrdpsData.get("etrdpsDt"));
			if(dtl.get("prdtGrp").isEmpty()) {
				dtl.put("prdtGrp", etrdpsData.get("prdtGrp"));
			}
			if(billData != null) {
				dtl.put("exprtnDt",billData.get("exprtnDt")); //	종료일	어음계정일 경우 : 만기일
			}
			ar05Mapper.updateEtrdpsDtl(dtl);
			ar05Mapper.insertEtrdpsDtl(dtl);
		}
		if((Boolean)paramMap.get("isAdvPay").equals(true)) {
			etrdpsData.put("diffAmt", paramMap.get("diffAmt").toString());
			ar05Mapper.insertAdvPay(etrdpsData);
		}
		/*
		Map<String, String> paramMapMatch = new HashMap<String, String>();
		paramMapMatch.put("clntCd", etrdpsData.get("clntCd"));
		paramMapMatch.put("coCd", etrdpsData.get("coCd"));
		paramMapMatch.put("prdtGrp", etrdpsData.get("prdtGrp"));
		ar02Mapper.callSaleMatch(paramMapMatch);
		*/
		return result;
		
	}
	
	@Override
	public int updateEtrdps(Map<String, Object> paramMap) {
		Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
		Map<String, String> billData = (Map<String, String>) paramMap.get("billData");
		// 원래 금액을 가져온다.
		Map<String, String> etrdpsInfo = ar05Mapper.selectEtrdpsInfo(etrdpsData);

		  int result = 0; //마감 체크 
		  if(checkEtrdpsClose(etrdpsData)) { 
			  return 500; 
		  }
		  
		  if(billData != null) { // 결제방법이 어음일때 
			  ar05Mapper.updateBill(billData);
		  }
		  result = ar05Mapper.updateEtrdps(etrdpsData);
		  
		  result = ar05Mapper.updateEtrdpsDt(etrdpsData);
		  
		  // 입금은 금액수정이 없음으로 한도에 반영안함.
		  
		  // 매핑을 수정할 경우 사용

		  if ("C".equals(paramMap.get("calcelMatchType"))) {
			  
			  Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			  Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
			  List<Map<String, String>> dtlParam = gson.fromJson((String)paramMap.get("detailArr"), dtlMap);
			  
			  for(Map<String, String> dtl : dtlParam) {
				  dtl.put("userId",  etrdpsData.get("userId"));
				  dtl.put("pgmId",   etrdpsData.get("pgmId"));
				  dtl.put("etrdpsSeq", etrdpsData.get("etrdpsSeq"));
				  dtl.put("etrdpsDt", etrdpsData.get("etrdpsDt"));
				  if(dtl.get("prdtGrp").isEmpty()) {
					  dtl.put("prdtGrp", etrdpsData.get("prdtGrp"));
				  }
				  if(billData != null) {
					  dtl.put("exprtnDt",billData.get("exprtnDt")); //	종료일	어음계정일 경우 : 만기일
				  }
				  ar05Mapper.updateEtrdpsDtl(dtl);
				  ar05Mapper.insertEtrdpsDtl(dtl);
			  }
			  if((Boolean)paramMap.get("isAdvPay").equals(true)) {
				  etrdpsData.put("diffAmt", paramMap.get("diffAmt").toString());
				  // ar05Mapper.insertAdvPay(etrdpsData);
				  ar05Mapper.updateAdvPay(etrdpsData);
			  }else {
				  ar05Mapper.deleteAdvPay(etrdpsData);
			  }
			  
		  }
		  /*
		  Map<String, String> paramMapMatch = new HashMap<String, String>();
		  paramMapMatch.put("clntCd", etrdpsData.get("clntCd"));
		  paramMapMatch.put("coCd", etrdpsData.get("coCd"));
		  paramMapMatch.put("prdtGrp", etrdpsData.get("prdtGrp"));
		  ar02Mapper.callSaleMatch(paramMapMatch);
		  */
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
		long etrdpsAmt = Long.parseLong(etrdpsInfo.get("etrdpsAmt"));
		String etrdpsDt = etrdpsInfo.get("etrdpsDt").replaceAll("-", "");
		
		cdtlnData.put("clntCd", clntCd);
		cdtlnData.put("etrdpsAmt", -1 * etrdpsAmt);
		cdtlnData.put("etrdpsDt", etrdpsDt);
		ar05Mapper.callCreditLoan(cdtlnData);
		
		// 수금 삭제
		if(etrdpsInfo.get("bilNo") != null) {
			// 결제방법이 어음일때 어음 삭제
			paramMap.put("bilNo", etrdpsInfo.get("bilNo"));
			ar05Mapper.deleteBill(paramMap);          // 어음삭제
		}
		ar05Mapper.updateEtrdpsDtlDelete(paramMap);  // 매출에 매핑 금액 삭제, ETRDPS_YN = 'N', ETRDPS_AMT = 나머지 매칭 금액 합계
		ar05Mapper.deleteEtrdpsDtl(paramMap);        // TB_AR05D02 삭제
		result = ar05Mapper.deleteEtrdps(paramMap);  // 입금 삭제
		return result;
	}

	
	@Override
	public int calcelMatch(Map<String, String> paramMap) {
		int result = 0;		
		ar05Mapper.updateEtrdpsDtlDelete(paramMap);  // 매출에 매핑 금액 삭제, ETRDPS_YN = 'N', ETRDPS_AMT = 나머지 매칭 금액 합계
		result = ar05Mapper.deleteEtrdpsDtl(paramMap);    // TB_AR05D02 삭제
		ar05Mapper.insertAdvPay(paramMap);
		
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
		// 마지막 마감일체크ㅡ하여 그 이전이면 수정불가
		if(sd07resultMax != null) {
			int maxEtrdpsCloseDay = Integer.parseInt(sd07resultMax.get("maxEtrdpsCloseDay").replace("-", ""));		
			if( maxEtrdpsCloseDay > Integer.parseInt(etrdpsDt)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int selectEtrdpsMapCount(Map<String, String> paramMap) {
		return ar05Mapper.selectEtrdpsMapCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectEtrdpsMap(Map<String, String> paramMap) {
		return ar05Mapper.selectEtrdpsMap(paramMap);
	}
	
	//더존 연동 관련 데이터 update 20220630 kdm 
	@Override
	public int updateDzSndSeq(Map<String, String> paramMap) {
		try {
			ar05Mapper.updateDzSndSeq(paramMap);
		}catch(Exception e ) {
			
			return 0;
			
		}
		return 1;
	}
}