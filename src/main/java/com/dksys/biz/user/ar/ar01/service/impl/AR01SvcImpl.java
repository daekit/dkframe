package com.dksys.biz.user.ar.ar01.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.ar.ar01.mapper.AR01Mapper;
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.pp.pp04.mapper.PP04Mapper;
import com.dksys.biz.user.sd.sd04.mapper.SD04Mapper;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sd.sd08.mapper.SD08Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.util.ExceptionThrower;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR01SvcImpl implements AR01Svc {
    
    @Autowired
    AR01Mapper ar01Mapper;
    
    @Autowired
    AR02Mapper ar02Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Autowired
    SD04Mapper sd04Mapper;
    
    @Autowired
    SD07Mapper sd07Mapper;
    
    @Autowired
    SD08Mapper sd08Mapper;
    
    @Autowired
    BM01Mapper bm01Mapper;
    
    @Autowired
    PP04Mapper pp04Mapper;
    
    @Autowired
    AR01Svc ar01Svc;
    
    @Autowired
    AR02Svc ar02Svc;

    @Autowired
    CM08Svc cm08Svc;
    
    @Autowired
    ExceptionThrower thrower;
    
	@Override
	public int insertShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		boolean isOdr = false;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		
		// 주문 insert
		if("".equals(paramMap.get("odrSeq"))) {
			isOdr = true;
			paramMap.put("totQty", paramMap.get("shipTotQty"));
			paramMap.put("totWt", paramMap.get("shipTotQty"));
			paramMap.put("totAmt", paramMap.get("shipTotAmt"));
			paramMap.put("odrRmk", paramMap.get("dlvrRmk"));
			// 출하요청서는 개별로 수입여부와 제조사를 관리하므로 첫번째를 추출하여 주문 insert
			paramMap.put("impYn", detailList.get(0).get("impYn"));
			paramMap.put("makrCd", detailList.get(0).get("makrCd"));
			sd04Mapper.insertOrder(paramMap);
		}
		
		// 출하메인 insert
		int result = ar01Mapper.insertShip(paramMap);
		// 출하상세 delete
		ar01Mapper.deleteShipDetail(paramMap);
		// 출하상세 insert
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			paramMap.put("impYn", detailMap.get("impYn"));
			paramMap.put("prdtSize", detailMap.get("prdtSize"));
			paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
			paramMap.put("prdtLen", detailMap.get("prdtLen"));
			paramMap.put("impYn", detailMap.get("impYn"));
			
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("pchsUpr", "0");
				detailMap.put("sellUpr", "0");
				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				detailMap.put("sellUpr", stockInfo.get("sellUpr"));
				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("ordrgDtlSeq", paramMap.get("ordrgDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			if(isOdr) {
				detailMap.put("odrQty", detailMap.get("shipQty"));
				detailMap.put("odrWt", detailMap.get("shipWt"));
				detailMap.put("odrUpr", detailMap.get("shipUpr"));
				detailMap.put("odrAmt", detailMap.get("shipAmt"));
				detailMap.put("odrDtlRmk", detailMap.get("shipDtlRmk"));
				sd04Mapper.insertOrderDetail(detailMap);
			}
			ar01Mapper.insertShipDetail(detailMap);
		}
		if(isOdr) {
			cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
		}
		cm08Svc.uploadFile("TB_AR01M01", paramMap.get("shipSeq"), mRequest);
		return result;
	}

	@Override
	public int selectShipCount(Map<String, String> paramMap) {
		return ar01Mapper.selectShipCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectShipList(Map<String, String> paramMap) {
		return ar01Mapper.selectShipList(paramMap);
	}

	@Override
	public int deleteShip(Map<String, String> paramMap) {
		
		Map<String, String> ar01Map = pp04Mapper.selectAr01MList(paramMap);
		
		if(ar01Map.containsKey("loadOrgNo")) {
			
			ar01Map.put("ERP_UPDATE_FLAG", "N");
			String loadOrgNo = "";
			String loadOrgNoGroup = MapUtils.getString(ar01Map, "loadOrgNo");
			String factoryCode = loadOrgNoGroup.substring(0, 1);
			String mesFtrCd = "";
			
			
			if(factoryCode.equals("C")) {
				mesFtrCd = "MES_KMCN";
			}else if(factoryCode.equals("N")) {
				mesFtrCd = "MES_KMIC";
			}else if(factoryCode.equals("J")) {
				mesFtrCd = "MES_KMJC";
			}
			
			// 개발계 
			// mesFtrCd = mesFtrCd + "_DEV";
			
			String[] loadOrgNoList = loadOrgNoGroup.split(",");
			for(int l = 0; l < loadOrgNoList.length; l++) {
				loadOrgNo += "'" + loadOrgNoList[l] + "'";
				
				if(l != loadOrgNoList.length-1) {
					loadOrgNo += ",";
				}
			}
			
			ar01Map.put("MES_FTR_CD", mesFtrCd);
			ar01Map.put("LOAD_ORG_NO", loadOrgNo);
			
			pp04Mapper.updateMesErpFlag(ar01Map);
		}
		
		int result = ar01Mapper.deleteShip(paramMap);
		result += ar01Mapper.deleteShipDetail(paramMap);
		return result;
	}

	@Override
	public Map<String, Object> selectShipInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("fileTrgtType", "TB_AR01M01");
		fileMap.put("fileTrgtKey", paramMap.get("shipSeq"));
		returnMap.put("fileList", cm08Svc.selectFileList(fileMap));
		returnMap.put("shipInfo", ar01Mapper.selectShipInfo(paramMap));
		returnMap.put("shipDetail", ar01Mapper.selectShipDetail(paramMap));
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getOrderInfo(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("shipInfo", ar01Mapper.getOrderInfo(paramMap));
		returnMap.put("shipDetail", ar01Mapper.getOrderDetail(paramMap));
		return returnMap;
	}

	@Override
	public int updateShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = ar01Mapper.updateShip(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 출하상세 delete
		ar01Mapper.deleteShipDetail(paramMap);
		// 출하상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			paramMap.put("prdtSize", detailMap.get("prdtSize"));
			paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
			paramMap.put("prdtLen", detailMap.get("prdtLen"));
			paramMap.put("impYn", detailMap.get("impYn"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("ordrgDtlSeq", paramMap.get("ordrgDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.insertShipDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_AR01M01", paramMap.get("shipSeq"), mRequest);
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		
		// 출하요청관리 상세페이지에서 수정시 현재 갖고있는 출하 리스트를 불러와서 출하여부를 확인 후 메인 출하 테이블의 출하 값을 변경하는 로직 실행
		List<Map<String, String>> detailCheckList = ar01Mapper.selectShipDetail(paramMap);
		
		if(detailCheckList.size() > 0) {
			
			Boolean check = true;
			
			for(Map<String, String> detailCheck : detailCheckList) {
				String shipYn = MapUtils.getString(detailCheck, "shipYn");
				if("N".equals(shipYn)) {
					check = false;
				}
			}
			
			if(check) {
				ar01Mapper.updateConfirm(paramMap);
			}
			
		}
		
		return result;
	}
	
	@Override
	public void updateSalesMng(Map<String, String> paramMap) {
		// 출하요청 담당자 update
		ar01Mapper.updateSalesMng(paramMap);
		
		// 매출 담당자 update
		Map<String, String> trspMap = new HashMap<String, String>();
		trspMap.put("trstRprcSeq", paramMap.get("shipSeq"));
		trspMap.put("pgmId", paramMap.get("pgmId"));
		trspMap.put("userId", paramMap.get("userId"));
		trspMap.put("salesMng", paramMap.get("salesMng"));
		ar02Mapper.updateSalesMng(trspMap);
	}
	
	@Override
	public void updateShipRmk(Map<String, String> paramMap) {
		// 출하요청 비고 update
		ar01Mapper.updateShipRmk(paramMap);
		
		// 매출 비고 update
		if(paramMap.containsKey("shipRmk")) {
		// 납기비고가 아닌 매출비고를 update시
			Map<String, String> trspMap = new HashMap<String, String>();
			trspMap.put("trstRprcSeq", paramMap.get("shipSeq"));
			trspMap.put("pgmId", paramMap.get("pgmId"));
			trspMap.put("userId", paramMap.get("userId"));
			trspMap.put("selpchCd", "SELPCH2");
			trspMap.put("trspRmk", paramMap.get("shipRmk"));
			ar02Mapper.updateTrspRmk(trspMap);
		}
	}

	//출하확정 수정시 updateConfirmToMes도 같이 수정해주어야함.
	@Override
	public void updateConfirm(Map<String, String> paramMap) throws Exception{
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		
		// 마감 체크
		if(!ar02Svc.checkSellClose(paramMap)) {
    		thrower.throwCommonException("sellClose");
		}
		
		/* 여신 체크 start */
		List<Map<String, Object>> loanList = new ArrayList<Map<String,Object>>();
		
		// 그룹별 금액 Map 
		Map<String, Object> grpAmtMap = new LinkedHashMap<String, Object>();
		for(Map<String, String> detailMap : detailList) {
			String prdtGrp = bm01Mapper.selectProductGroup(detailMap.get("prdtCd"));
			if(grpAmtMap.containsKey(prdtGrp)) {
				grpAmtMap.put(prdtGrp, (Long) grpAmtMap.get(prdtGrp) + Long.parseLong(detailMap.get("realShipAmt")));
			}else {
				grpAmtMap.put(prdtGrp, Long.parseLong(detailMap.get("realShipAmt")));
			}
		}
		
		// 여신 Map
		int bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
		for(Map.Entry<String, Object> entry : grpAmtMap.entrySet()) {
			String prdtGrp = entry.getKey();
			Long totAmt = (Long) entry.getValue();
			Map<String, Object> loanMap = new HashMap<String, Object>();
			loanMap.put("coCd", paramMap.get("coCd"));
			loanMap.put("clntCd", paramMap.get("clntCd"));
			loanMap.put("prdtGrp", prdtGrp);
			loanMap.put("trstDt", paramMap.get("dlvrDttm"));
			long bilgVatAmt = (long) Math.floor(totAmt * bilgVatPer / 100);
			loanMap.put("totAmt", totAmt + bilgVatAmt);
			loanList.add(loanMap);
		}
		
		// 그룹별 여신 리스트를 순회하며 여신 체크
		for(Map<String, Object> loanMap : loanList) {
			long diffLoan = ar02Svc.checkLoan(loanMap);
			if(diffLoan < 0) {
	        	thrower.throwCreditLoanException(loanMap.get("prdtGrp").toString(), diffLoan);
	        }
		}
		/* 여신 체크 end */
		
		String clntCd = paramMap.get("clntCd");
		String clntNm = paramMap.get("clntNm");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.putAll(ar01Mapper.selectShipDetailInfo(detailMap));
			detailMap.put("shipProcId", paramMap.get("userId"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			
			if("Y".equals(detailMap.get("shipYn"))) {
			// 이미 확정된 상세내역이면 확정 불가능
				thrower.throwCommonException("alreadyConfirm");
			}
			
			// 출하 확정
			ar01Mapper.updateConfirmDetail(detailMap);
			
			// 커플러일 경우 별도 단가 데이터 저장
			Map<String, String> coupleMap = new HashMap<String, String>();
			if(detailMap.get("prdtDiv").contains("PRDTDIV22")) {
				coupleMap.put("coCd", paramMap.get("coCd"));
				coupleMap.put("clntCd", clntCd);
				coupleMap.put("selpchCd", "SELPCH2");
				coupleMap.put("prdtCd", detailMap.get("prdtCd"));
				coupleMap.put("prdtDt", paramMap.get("reqDt"));
				coupleMap.put("prdtUpr", detailMap.get("realShipUpr"));
				coupleMap.put("rmk", paramMap.get("shipRmk"));
				coupleMap.put("userId", paramMap.get("userId"));
				coupleMap.put("pgmId", paramMap.get("pgmId"));
				sd08Mapper.insertCplrUntpc(coupleMap);
			}
			
			// 매출정보 insert
			paramMap.putAll(detailMap);
			paramMap.put("trstDt", 		 paramMap.get("dlvrDttm").replace("-", ""));
			paramMap.put("pchsUpr", 	 "0");
			paramMap.put("sellUpr",      detailMap.get("realShipUpr"));
			paramMap.put("stockUpr",     detailMap.get("stockUpr"));
			paramMap.put("trstQty",      detailMap.get("shipQty"));
			paramMap.put("trstWt",       detailMap.get("shipWt"));
			paramMap.put("trstUpr",      detailMap.get("shipUpr"));
			paramMap.put("trstAmt",      detailMap.get("shipAmt"));
			paramMap.put("realTrstQty",  detailMap.get("realShipQty"));
			paramMap.put("realTrstWt",   detailMap.get("realShipWt"));
			paramMap.put("realTrstUpr",  detailMap.get("realShipUpr"));
			paramMap.put("realTrstAmt",  detailMap.get("realShipAmt"));
			paramMap.put("bilgQty",      detailMap.get("realShipQty"));
			paramMap.put("bilgWt",       detailMap.get("realShipWt"));
			paramMap.put("bilgUpr",      detailMap.get("realShipUpr"));
			paramMap.put("bilgAmt",      detailMap.get("realShipAmt"));
			paramMap.put("clntCd",       clntCd);
			paramMap.put("clntNm",       clntNm);
			paramMap.put("prdtSpec", 	 detailMap.get("prdtSpec"));	
			paramMap.put("prdtSize", 	 detailMap.get("prdtSize"));		
			paramMap.put("trspTypCd", 	 "TRSPTYP1"); // 정상매출
			paramMap.put("trstRprcSeq",  detailMap.get("shipSeq"));		
			paramMap.put("trstDtlSeq", 	 detailMap.get("shipDtlSeq"));				  	
			paramMap.put("odrNo", 		 paramMap.get("odrSeq"));
			paramMap.put("makrCd", 	     detailMap.get("makrCd"));
			paramMap.put("trspRmk", 	 paramMap.get("shipRmk"));
			paramMap.put("subClntNm", "");
			paramMap.put("subClntCd", "");
			// 부가세
			long bilgVatAmt = (long) Math.floor(Long.parseLong(detailMap.get("realShipAmt")) * bilgVatPer / 100);
			paramMap.put("bilgVatAmt", 	String.valueOf(bilgVatAmt));
			
			// 실수, 정수 모두를 할당해야 하므로 BigDecimal로 선언
			BigDecimal bilgQty = new BigDecimal(paramMap.get("bilgQty"));
			BigDecimal bilgWt = new BigDecimal(paramMap.get("bilgWt"));
			BigDecimal bilgAmt = new BigDecimal(paramMap.get("bilgAmt"));
			
			if(bilgQty.compareTo(BigDecimal.ZERO) != 0 || bilgWt.compareTo(BigDecimal.ZERO) != 0 || bilgAmt.compareTo(BigDecimal.ZERO) != 0){
			// 청구수량, 청구중량, 청구금액이 모두 0이면 insert하지 않는다.
				ar02Mapper.insertPchsSell(paramMap);
			}
			
			if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
			{
				// 재고정보 update
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
				}
				
				paramMap.put("prdtCd", detailMap.get("prdtCd"));
				paramMap.put("prdtSize", detailMap.get("prdtSize"));
				paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
				paramMap.put("prdtLen", detailMap.get("prdtLen"));
				paramMap.put("impYn", detailMap.get("impYn"));
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				paramMap.put("stockChgCd", "STOCKCHG02");
				if(stockInfo == null) {
					paramMap.put("pchsUpr", detailMap.get("realShipUpr"));
					paramMap.put("sellUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockUpr", detailMap.get("realShipUpr"));
					paramMap.put("stdUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockQty", String.valueOf(Double.parseDouble(detailMap.get("realShipQty"))*-1));
					paramMap.put("stockWt" , String.valueOf(Double.parseDouble(detailMap.get("realShipWt"))*-1));
				} else {
					paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
					paramMap.put("sellUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockUpr", stockInfo.get("stockUpr"));
					paramMap.put("stdUpr", stockInfo.get("stdUpr"));
					double stockQty = Double.parseDouble(stockInfo.get("stockQty")) - Double.parseDouble(detailMap.get("realShipQty"));
					double stockWt  = Double.parseDouble(stockInfo.get("stockWt"))  - Double.parseDouble(detailMap.get("realShipWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt" , String.valueOf(stockWt));
				}
				sm01Mapper.updateStockSell(paramMap);
			}
			
			
			Map<String, String> matchMap = new HashMap<String, String>();
			matchMap.putAll(paramMap);
			matchMap.put("clntCd", clntCd);
			matchMap.put("prdtGrp", detailMap.get("prdtGrp"));
	        ar02Mapper.callSaleMatch(matchMap);
			
		}
		
		if(selectConfirmCount(paramMap) == selectDetailCount(paramMap)) {
			ar01Mapper.updateConfirm(paramMap);
		}
		
		// 최종 여신 체크 / 여신 차감
		for(Map<String, Object> loanMap : loanList) {
			long diffLoan = ar02Svc.checkLoan(loanMap);
			if(diffLoan < 0) {
	        	thrower.throwCreditLoanException(loanMap.get("prdtGrp").toString(), diffLoan);
	        }else {
	        	// 여신 차감후 음수 return시 롤백 
	        	long loanPrcsResult = ar02Svc.deductLoan(loanMap);
	        	if(loanPrcsResult < 0) {
	    			throw new Exception();
	    		}
	        }
		}
	}
	
	/*
	 * 여신체크 일괄변경으로 인한 주석처리 - 20210630
	@Override
	public int updateConfirmToMes(Map<String, String> paramMap, List<Map<String, String>> detailList) {
		//마감 체크
		int result = 0;
		long realTotTrstAmt = 0;
		result = detailList.size();
		String clntCd = paramMap.get("clntCd");
		String clntNm = paramMap.get("clntNm");
		String userId = paramMap.get("loginId");
		String pgmId = paramMap.get("pgmId");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("login_Id", userId);
			detailMap.put("ship_Proc_Id", userId);
			detailMap.put("user_id", userId);
			detailMap.put("pgm_id", pgmId);
			detailMap.put("ship_Seq", paramMap.get("shipSeq"));
			//커플러일 경우 별도 단가 데이터 저장
			if(detailMap.get("prdtStkn") != null) {
				if(detailMap.get("prdt_Stkn").contains("PDSKCP")) {
					detailMap.put("co_Cd", paramMap.get("coCd"));
					detailMap.put("clnt_Cd", paramMap.get("clntCd"));
					detailMap.put("selpch_Cd", "SELPCH2");
					detailMap.put("prdt_Dt", paramMap.get("reqDt"));
					detailMap.put("prdt_Upr", detailMap.get("realShipUpr"));
					detailMap.put("rmk", paramMap.get("shipRmk"));
					sd08Mapper.insertCplrUntpc(detailMap);
				}
			}
			ar01Mapper.updateConfirmDetail(detailMap);
			//매출정보 insert
			detailMap = ar01Mapper.selectShipDetailInfo(detailMap);
			paramMap.putAll(detailMap);
			paramMap.put("trstDt", 		paramMap.get("dlvrDttm").replace("-", "").substring(0,8));
			paramMap.put("pchsUpr", 	"0");
			paramMap.put("sellUpr",     detailMap.get("realShipUpr"));
			paramMap.put("stockUpr",    detailMap.get("stockUpr"));
			paramMap.put("trstQty",     detailMap.get("shipQty"));
			paramMap.put("trstWt",      detailMap.get("shipWt"));
			paramMap.put("trstUpr",     detailMap.get("shipUpr"));
			paramMap.put("trstAmt",     detailMap.get("shipAmt"));
			paramMap.put("realTrstQty", detailMap.get("realShipQty"));
			paramMap.put("realTrstWt",  detailMap.get("realShipWt"));
			paramMap.put("realTrstUpr", detailMap.get("realShipUpr"));
			paramMap.put("realTrstAmt", detailMap.get("realShipAmt"));
			realTotTrstAmt += Long.parseLong(detailMap.get("realShipAmt"));
			paramMap.put("bilgQty",     detailMap.get("realShipQty"));
			paramMap.put("bilgWt",      detailMap.get("realShipWt"));
			paramMap.put("bilgUpr",     detailMap.get("realShipUpr"));
			paramMap.put("bilgAmt",     detailMap.get("realShipAmt"));
			paramMap.put("clntCd",      clntCd);
			paramMap.put("clntNm",      clntNm);
			paramMap.put("prdtSpec", 	detailMap.get("prdtSpec"));	
			paramMap.put("prdtSize", 	detailMap.get("prdtSize"));		
			paramMap.put("trspTypCd", 	"TRSPTYP1");              // 정상매출
			paramMap.put("trstRprcSeq", detailMap.get("shipSeq"));		
			paramMap.put("trstDtlSeq", 	detailMap.get("shipDtlSeq"));				  	
			paramMap.put("odrNo", 		paramMap.get("odrSeq"));
			paramMap.put("trspRmk", 	paramMap.get("shipRmk"));
			paramMap.put("selpchCd", "SELPCH2"); // 매출데이터
			long bilgVatAmt = ar02Mapper.selectBilgVatAmt(paramMap);
			paramMap.put("bilgVatAmt", 	String.valueOf(bilgVatAmt));
			realTotTrstAmt += bilgVatAmt;
			paramMap.put("mngTel", "");
			paramMap.put("userId", userId);
			paramMap.put("pgmId", pgmId);
			ar02Mapper.insertPchsSell(paramMap);
			
			if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
			{
				// 재고정보 update
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
				}
				paramMap.put("clntCd", clntCd);
				paramMap.put("clntNm", clntNm);
				
				paramMap.put("prdtCd", detailMap.get("prdtCd"));
				paramMap.put("prdtSize", detailMap.get("prdtSize"));
				paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
				paramMap.put("prdtLen", detailMap.get("prdtLen"));
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				paramMap.put("stockChgCd", "STOCKCHG02");
				if(stockInfo == null) {
					paramMap.put("pchsUpr", detailMap.get("realShipUpr"));
					paramMap.put("sellUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockUpr", detailMap.get("realShipUpr"));
					paramMap.put("stdUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockQty", "-"+detailMap.get("realShipQty"));
					paramMap.put("stockWt" , "-"+detailMap.get("realShipWt"));
				} else {
					paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
					paramMap.put("sellUpr", detailMap.get("realShipUpr"));
					paramMap.put("stockUpr", stockInfo.get("stockUpr"));
					paramMap.put("stdUpr", stockInfo.get("stdUpr"));
					int stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realShipQty"));
					int stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  - Integer.parseInt(detailMap.get("realShipWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt", String.valueOf(stockWt));
				}
				paramMap.put("impYn", "N");
				sm01Mapper.updateStockSell(paramMap);
			}
		}
		// 여신 체크
		paramMap.put("realTotTrstAmt", String.valueOf(realTotTrstAmt));
		paramMap.put("clntCd",  clntCd);
		if(ar02Svc.checkLoan(paramMap)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		if(selectConfirmCount(paramMap) == selectDetailCount(paramMap)) {
			ar01Mapper.updateConfirm(paramMap);
		}
		return result;
	}
	*/

	@Override
	public int selectConfirmCount(Map<String, String> paramMap) {
		return ar01Mapper.selectConfirmCount(paramMap);
	}
	

	@Override
	public int selectDetailCount(Map<String, String> paramMap) {
		return ar01Mapper.selectDetailCount(paramMap);
	}

	@SuppressWarnings("null")
	@Override
	public void updateCancel(Map<String, String> paramMap) throws Exception{
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		
		// 마감 체크
		if(!ar02Svc.checkSellClose(paramMap)) {
    		thrower.throwCommonException("sellClose");
		}
		String clntCd = paramMap.get("clntCd");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.putAll(ar01Mapper.selectShipDetailInfo(detailMap));
			
			if("N".equals(detailMap.get("shipYn"))) {
			// 이미 취소된 상세내역이면 취소 불가능.
				thrower.throwCommonException("alreadyCancel");
			}
			
			Map<String, String> trstMap = new HashMap<String, String>();
			trstMap.put("trstRprcSeq", paramMap.get("shipSeq"));
			trstMap.put("trstDtlSeq", detailMap.get("shipDtlSeq"));
			trstMap.put("pgmId", paramMap.get("pgmId"));
			List<Map<String, String>> bilgList = ar02Mapper.checkBilg(trstMap);
			for (Map<String, String> map : bilgList) {
				if(map != null && Integer.parseInt(map.get("bilgCertNo")) != 0) {
				// 이미 전표처리가 되었으면 취소 불가능.
					thrower.throwCommonException("bilgComplete");
				}
			}
			
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.updateCancelDetail(detailMap);
			// ar02Mapper.deletePchsSell(trstMap);
			

			List<Map<String, Object>> trstCertNoList = ar02Mapper.selectTrstCertiNo(trstMap);
			
			if(trstCertNoList.size() != 0) {
				for(int i=0; i<trstCertNoList.size(); i++) {
					detailMap.put("trstCertiNo", MapUtils.getString(trstCertNoList.get(i), "TRST_CERTI_NO"));
					
					List<Map<String, String>> countSellList = ar02Mapper.countSellList(detailMap);
					
					if(countSellList.size() != 0) {
						for(int j=0; j<countSellList.size(); j++) {
							detailMap.put("etrdpsSeqFind", MapUtils.getString(countSellList.get(j), "ETRDPS_SEQ"));
							int countSell = ar02Mapper.countSellFind(detailMap);
							detailMap.put("countSell", String.valueOf(countSell));
							ar02Mapper.deleteSell(detailMap);
						}
					}
					
					ar02Mapper.deleteSellReal(detailMap);
					
					/*
					int countSell = ar02Mapper.countSell(detailMap);
					detailMap.put("countSell", String.valueOf(countSell));
					
					ar02Mapper.deleteSell(detailMap);
					*/
				}
			}
		
			
			//재고원복
			if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
			{
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
				}
				paramMap.put("prdtCd", detailMap.get("prdtCd"));
				paramMap.put("prdtSize", detailMap.get("prdtSize"));
				paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
				paramMap.put("prdtLen", detailMap.get("prdtLen"));
				paramMap.put("impYn", detailMap.get("impYn"));
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				
				if(stockInfo == null) {
					stockInfo = new HashMap<String, String>();
					stockInfo.put("stockQty", "0");
					stockInfo.put("stockWt", "0");
				}
				
				double stockQty = Double.parseDouble(stockInfo.get("stockQty")) + Double.parseDouble(detailMap.get("realShipQty"));
				double stockWt = Double.parseDouble(stockInfo.get("stockWt")) + Double.parseDouble(detailMap.get("realShipWt"));
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt", String.valueOf(stockWt));
				sm01Mapper.updateStockCancel(paramMap);
			}

			Map<String, String> matchMap = new HashMap<String, String>();
			matchMap.putAll(paramMap);
			matchMap.put("clntCd", clntCd);
			matchMap.put("prdtGrp", detailMap.get("prdtGrp"));
	        ar02Mapper.callSaleMatch(matchMap);
			
		}
		
		ar01Mapper.updateCancel(paramMap);
		
		/* 그룹별 여신 리스트 생성 start */
		List<Map<String, Object>> loanList = new ArrayList<Map<String,Object>>();
		
		// 그룹별 금액 Map 
		Map<String, Object> grpAmtMap = new LinkedHashMap<String, Object>();
		for(Map<String, String> detailMap : detailList) {
			String prdtGrp = bm01Mapper.selectProductGroup(detailMap.get("prdtCd"));
			if(grpAmtMap.containsKey(prdtGrp)) {
				grpAmtMap.put(prdtGrp, (Long) grpAmtMap.get(prdtGrp) + Long.parseLong(detailMap.get("realShipAmt")));
			}else {
				grpAmtMap.put(prdtGrp, Long.parseLong(detailMap.get("realShipAmt")));
			}
		}
		
		// 여신 Map
		int bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
		for(Map.Entry<String, Object> entry : grpAmtMap.entrySet()) {
			String prdtGrp = entry.getKey();
			Long totAmt = (Long) entry.getValue();
			Map<String, Object> loanMap = new HashMap<String, Object>();
			loanMap.put("coCd", paramMap.get("coCd"));
			loanMap.put("clntCd", paramMap.get("clntCd"));
			loanMap.put("prdtGrp", prdtGrp);
			loanMap.put("trstDt", paramMap.get("dlvrDttm"));
			long bilgVatAmt = (long) Math.floor(totAmt * bilgVatPer / 100);
			loanMap.put("totAmt", totAmt + bilgVatAmt);
			loanList.add(loanMap);
		}
		/* 그룹별 여신 리스트 생성 end */
		
		// 그룹별 여신 리스트를 순회하며 여신 원복
		for(Map<String, Object> loanMap : loanList) {
			// 여신 원복후 음수 return시 롤백 
	    	long loanPrcsResult = ar02Svc.depositLoan(loanMap);
	    	if(loanPrcsResult < 0) {
				throw new Exception();
			}
		}
	}

	@Override
	@SuppressWarnings("all")
	public int updateRecptYn(Map<String, Object> param) {
		int result = 0;
		List<Map<String, String>> detailList = (List<Map<String, String>>) param.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += ar01Mapper.updateRecptYn(detailMap);
		}
		return result;
	}
}