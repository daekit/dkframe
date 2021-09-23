package com.dksys.biz.user.od.od01.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.ifsys.mes.mapper.MESSTOCKMapper;
import com.dksys.biz.user.od.od01.mapper.OD01Mapper;
import com.dksys.biz.user.od.od01.service.OD01Svc;
import com.dksys.biz.user.od.od04.mapper.OD04Mapper;
import com.dksys.biz.user.sd.sd04.mapper.SD04Mapper;
import com.dksys.biz.user.sd.sd08.mapper.SD08Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.util.ExceptionThrower;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class OD01SvcImpl implements OD01Svc {
	
    @Autowired
    OD01Mapper od01Mapper;
    
    @Autowired
    OD04Mapper od04Mapper;
    
    @Autowired
    AR02Mapper ar02Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Autowired
    SD04Mapper sd04Mapper;

    @Autowired
    SD08Mapper sd08Mapper;

    @Autowired
    BM01Mapper bm01Mapper;
    
    @Autowired
    MESSTOCKMapper mesStockMapper;
    
    @Autowired
    AR02Svc ar02Svc;
    
    @Autowired
    CM08Svc cm08Svc;
    
    @Autowired
    ExceptionThrower thrower;
    
    @Override
	public int selectOrdrgCount(Map<String, String> paramMap) {
		return od01Mapper.selectOrdrgCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectOrdrgList(Map<String, String> paramMap) {
		return od01Mapper.selectOrdrgList(paramMap);
	}
	
	@Override
	public Map<String, Object> selectOrdrgInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("fileTrgtType", "TB_OD01M01");
		fileMap.put("fileTrgtKey", paramMap.get("ordrgSeq"));
		returnMap.put("fileList", cm08Svc.selectFileList(fileMap));
		returnMap.put("orderInfo", od01Mapper.selectOrdrgInfo(paramMap));
		returnMap.put("orderDetail", od01Mapper.selectOrdrgDetailList(paramMap));
		return returnMap;
	}
	
	@Override
	public int selectOrdrgDetailCount(Map<String, String> paramMap) {
		return od01Mapper.selectOrdrgDetailCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectOrdrgDetailList(Map<String, String> paramMap) {
		return od01Mapper.selectOrdrgDetailList(paramMap);
	}
	
	@Override
	public Map<String, Object> getOrderInfo(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("orderInfo", od01Mapper.getOrderInfo(paramMap));
		returnMap.put("orderDetail", od01Mapper.getOrderDetailList(paramMap));
		return returnMap;
	}
	
	@Override
	public int selectConfirmCount(Map<String, String> paramMap) {
		return od01Mapper.selectConfirmCount(paramMap);
	}
	
	@Override
	public int insertOrdrg(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		boolean isOdr = false;
		boolean isReq = false;
		if("".equals(paramMap.get("odrSeq"))) {
			isOdr = true;
//			paramMap.put("totQty", paramMap.get("totQty"));
//			paramMap.put("totWt",  paramMap.get("totWt"));
//			paramMap.put("totAmt", paramMap.get("totAmt"));
			paramMap.put("odrRmk", paramMap.get("ordrgRmk"));
			sd04Mapper.insertOrder(paramMap);
		}
		if(!"".equals(paramMap.get("reqSeq"))) {
			isReq = true;
		}
		int result = od01Mapper.insertOrdrg(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 발주상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			paramMap.put("prdtSize", detailMap.get("prdtSize"));
			paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
			paramMap.put("prdtLen", detailMap.get("prdtLen"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("pchsUpr", "0");
				detailMap.put("sellUpr", "0");
//				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				detailMap.put("sellUpr", stockInfo.get("sellUpr"));
//				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
            detailMap.put("reqSeq", paramMap.get("reqSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			if(isOdr) {
				detailMap.put("odrQty", detailMap.get("ordrgQty"));
				detailMap.put("odrWt", detailMap.get("ordrgWt"));
				detailMap.put("odrUpr", detailMap.get("ordrgUpr"));
				detailMap.put("odrAmt", detailMap.get("ordrgAmt"));
				sd04Mapper.insertOrderDetail(detailMap);
			}
			od01Mapper.insertOrdrgDetail(detailMap);
			if(isReq) {
				od04Mapper.updateReqOrder(detailMap);
			}
		}
		if(isOdr) {
			cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
		}
		if(isReq) {
			if(od04Mapper.selectOrdrgNCnt(paramMap) < 1) {
				od04Mapper.updateReqOrdrgY(paramMap);
			}
		}
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("ordrgSeq"), mRequest);
		return result;
	}
	
	@Override
	public int updateOrdrg(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = od01Mapper.updateOrdrg(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		od01Mapper.deleteOrdrgDetail(paramMap);
		// 발주상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			paramMap.put("prdtSize", detailMap.get("prdtSize"));
			paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
			paramMap.put("prdtLen", detailMap.get("prdtLen"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
            detailMap.put("reqSeq", paramMap.get("reqSeq"));
            detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			// 매입, 매출 하나라도 확정이 되었으면 수정으로 한다.
			if("Y".equals(detailMap.get("ordrgYn")) || "Y".equals(detailMap.get("shipYn"))) {
				od01Mapper.updateOrdrgDetail(detailMap);
			}else {
				od01Mapper.insertOrdrgDetail(detailMap);
			}			
		}
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("ordrgSeq"), mRequest);
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		return result;
	}
	
	@Override
	public void updateSalesMng(Map<String, String> paramMap) {
		// 발주 담당자 update
		od01Mapper.updateSalesMng(paramMap);
		
		// 매출/매입 담당자 update
		Map<String, String> trspMap = new HashMap<String, String>();
		trspMap.put("trstRprcSeq", paramMap.get("ordrgSeq"));
		trspMap.put("pgmId", paramMap.get("pgmId"));
		trspMap.put("userId", paramMap.get("userId"));
		trspMap.put("salesMng", paramMap.get("salesMng"));
		ar02Mapper.updateSalesMng(trspMap);
	}
	
	@Override
	public void updateOrdrgRmk(Map<String, String> paramMap) {
		// 발주 비고 update
		od01Mapper.updateOrdrgRmk(paramMap);
		
		// 매출/매입 비고 update
		Map<String, String> trspMap = new HashMap<String, String>();
		trspMap.put("trstRprcSeq", paramMap.get("ordrgSeq"));
		trspMap.put("pgmId", paramMap.get("pgmId"));
		trspMap.put("userId", paramMap.get("userId"));
		if(paramMap.containsKey("ordrgRmk")) {
		// 매입비고를 update시
			trspMap.put("selpchCd", "SELPCH1");
			trspMap.put("trspRmk", paramMap.get("ordrgRmk"));
		}else if(paramMap.containsKey("dlvrRmk")) {
		// 매출비고를 update시
			trspMap.put("selpchCd", "SELPCH2");
			trspMap.put("trspRmk", paramMap.get("dlvrRmk"));
		}
		ar02Mapper.updateTrspRmk(trspMap);
	}
	
	@Override
	public int deleteOrdrg(Map<String, String> paramMap) {
		int result = od01Mapper.deleteOrdrg(paramMap);
		result += od01Mapper.deleteOrdrgDetail(paramMap);
		return result;
	}
    
	@Override
	public void updateConfirm(Map<String, String> paramMap) throws Exception{
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		List<Map<String, Object>> loanList = new ArrayList<Map<String,Object>>();
		
		/*
		 * 1. 일괄 확정 (A)
		 *    - 매입확정 : 기존 매입이 (N)
		 *    - 직송일 경우 매출확정 : 기존 매출이 (N)
		 * 
		 * 2. 매입확정 (P)
		 *    - 매입확정 : 기존 매입이 (N)
		 *    
		 * 3. 매출확정 (S)   
		 *    - 직송일 경우 매출확정 : 기존 매출이 (N)
		 */
		
		/* 마감체크 부문 */
		// 매입 N 이면서  P 매입확정, A 일괄인 경우 매입확정
		if("P".equals(paramMap.get("comfirmType")) || "A".equals(paramMap.get("comfirmType"))) {
			// 마감 체크
			if(!ar02Svc.checkPchsClose(paramMap)) {
	    		thrower.throwCommonException("pchsClose");
			}
		}		
		
		// 직송이면서 매출여부 N인경우 매출확정 시작
         if("Y".equals(paramMap.get("dirtrsYn"))) {		
		       // 전체 확정인 경우는 진행
		       // 매출확정이면서 매입이 확정이 된경우 P 매입확정, S 매출확정, A 일괄
        	if ( "A".equals(paramMap.get("comfirmType")) || "S".equals(paramMap.get("comfirmType"))) {
        		
				// 마감 체크 (매출)
        		if(!ar02Svc.checkSellClose(paramMap)) {
            		thrower.throwCommonException("sellClose");
        		}
        		
        		/* 여신 체크 start */
        		
        		// 그룹별 금액 Map 
        		Map<String, Object> grpAmtMap = new LinkedHashMap<String, Object>();
        		for(Map<String, String> detailMap : detailList) {
        			if("N".equals(detailMap.get("shipYn"))) {
	        			String prdtGrp = bm01Mapper.selectProductGroup(detailMap.get("prdtCd"));
	        			if(grpAmtMap.containsKey(prdtGrp)) {
	        				grpAmtMap.put(prdtGrp, (Long) grpAmtMap.get(prdtGrp) + Long.parseLong(detailMap.get("shipAmt")));
	        			}else {
	        				grpAmtMap.put(prdtGrp, Long.parseLong(detailMap.get("shipAmt")));
	        			}
        			}
        		}
        		
        		// 여신 Map
        		Map<String, String> bilgVatPerMap = new HashMap<String, String>();
        		bilgVatPerMap.put("selpchCd", "SELPCH2");
        		bilgVatPerMap.put("clntCd", paramMap.get("sellClntCd"));
        		int bilgVatPer = ar02Mapper.selectBilgVatPer(bilgVatPerMap);
        		
        		for(Map.Entry<String, Object> entry : grpAmtMap.entrySet()) {
        			String prdtGrp = entry.getKey();
        			Long totAmt = (Long) entry.getValue();
        			Map<String, Object> loanMap = new HashMap<String, Object>();
        			loanMap.put("coCd", paramMap.get("coCd"));
        			loanMap.put("clntCd", paramMap.get("sellClntCd"));
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
        	}
        }
//-----------------------------------------------------------------------------------------------------------------------------------------------		
		String clntCd = paramMap.get("clntCd");
		String clntNm = paramMap.get("clntNm");
		String sellClntCd = paramMap.get("sellClntCd");
		String sellClntNm = paramMap.get("sellClntNm");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.putAll(od01Mapper.selectOrdrgDetailInfo(detailMap));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			
			if("P".equals(paramMap.get("comfirmType")) || "A".equals(paramMap.get("comfirmType"))) {
				if("Y".equals(detailMap.get("ordrgYn"))) {
				// 이미 매입확정된 상세내역이면 매입확정 불가능
					thrower.throwCommonException("alreadyConfirm");
				}
			}
			
			if ("Y".equals(paramMap.get("dirtrsYn"))){
				if("S".equals(paramMap.get("comfirmType")) || "A".equals(paramMap.get("comfirmType"))) {
					if("Y".equals(detailMap.get("shipYn"))) {
					// 이미 매출확정된 상세내역이면 매출확정 불가능
						thrower.throwCommonException("alreadyConfirm");
					}
				}
			}
			
			//커플러일 경우 별도 단가 데이터 저장
			Map<String, String> coupleMap = new HashMap<String, String>();
			if(detailMap.get("prdtDiv").contains("PRDTDIV22")) {
				coupleMap.put("coCd", paramMap.get("coCd"));
				coupleMap.put("clntCd", clntCd);
				coupleMap.put("selpchCd", "SELPCH1");
				coupleMap.put("prdtCd", detailMap.get("prdtCd"));
				coupleMap.put("prdtDt", paramMap.get("reqDt"));
				coupleMap.put("prdtUpr", detailMap.get("realDlvrUpr"));
				coupleMap.put("rmk", paramMap.get("ordrgRmk"));
				coupleMap.put("userId", paramMap.get("userId"));
				coupleMap.put("pgmId", paramMap.get("pgmId"));
				// 커플러 매입 단가 입력
				sd08Mapper.insertCplrUntpc(detailMap);
				if("Y".equals(paramMap.get("dirtrsYn"))) {
					coupleMap.put("clntCd", sellClntCd);
					coupleMap.put("selpchCd", "SELPCH2");
					coupleMap.put("prdtUpr", detailMap.get("shipUpr"));
					coupleMap.put("rmk", paramMap.get("dlvrRmk"));
					// 커플러 매출 단가 입력
					sd08Mapper.insertCplrUntpc(coupleMap);
				}
			}

			//매출매입 데이터 세팅
			paramMap.putAll(detailMap);
			paramMap.put("selpchCd",    "SELPCH1");
			paramMap.put("trstDt",       paramMap.get("dlvrDttm").replace("-", ""));
			paramMap.put("pchsUpr",      detailMap.get("realDlvrUpr"));
			paramMap.put("stockUpr",     detailMap.get("stockUpr"));
			paramMap.put("trstQty",      detailMap.get("ordrgQty"));
			paramMap.put("trstWt",       detailMap.get("ordrgWt"));
			paramMap.put("trstUpr",      detailMap.get("ordrgUpr"));
			paramMap.put("trstAmt",      detailMap.get("ordrgAmt"));
			paramMap.put("realTrstQty",  detailMap.get("realDlvrQty"));
			paramMap.put("realTrstWt",   detailMap.get("realDlvrWt"));
			paramMap.put("realTrstUpr",  detailMap.get("realDlvrUpr"));
			paramMap.put("realTrstAmt",  detailMap.get("realDlvrAmt"));
			paramMap.put("bilgQty",      detailMap.get("realDlvrQty"));
			paramMap.put("bilgWt",       detailMap.get("realDlvrWt"));
			paramMap.put("bilgUpr",      detailMap.get("realDlvrUpr"));
			paramMap.put("bilgAmt",      detailMap.get("realDlvrAmt"));
			paramMap.put("clntNm",       detailMap.get("clntNm"));
			paramMap.put("trstRprcSeq",  detailMap.get("ordrgSeq"));		
			paramMap.put("trstDtlSeq",   detailMap.get("ordrgDtlSeq"));				  	
			paramMap.put("odrNo", paramMap.get("odrSeq"));
			paramMap.put("clntCd", clntCd);
			paramMap.put("clntNm", clntNm);
			paramMap.put("trspRmk", paramMap.get("ordrgRmk"));
			long bilgVatAmt = ar02Mapper.selectBilgVatAmt(paramMap);
			paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt));
			
			//재고 세팅
			if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
			{
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
				}
				paramMap.put("prdtCd",   detailMap.get("prdtCd"));
				paramMap.put("prdtSize", detailMap.get("prdtSize"));
				paramMap.put("prdtSpec", detailMap.get("prdtSpec"));
				paramMap.put("prdtLen",  detailMap.get("prdtLen"));
				// 재고 정보 세팅
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				paramMap.put("stockChgCd", "STOCKCHG01");
				if(stockInfo == null) {
					paramMap.put("pchsUpr",  detailMap.get("realDlvrUpr"));
					paramMap.put("sellUpr", "0");
					paramMap.put("stockUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("stdUpr",   detailMap.get("realDlvrUpr"));
					paramMap.put("stockQty", detailMap.get("realDlvrQty"));
					paramMap.put("stockWt",  detailMap.get("realDlvrWt"));
				} else {
					paramMap.put("pchsUpr",  detailMap.get("realDlvrUpr"));
					paramMap.put("sellUpr",  stockInfo.get("sellUpr"));
					paramMap.put("stockUpr", stockInfo.get("stockUpr"));
					paramMap.put("stdUpr",   stockInfo.get("stdUpr"));
					double stockQty = Double.parseDouble(stockInfo.get("stockQty")) + Double.parseDouble(detailMap.get("realDlvrQty"));
					double stockWt  = Double.parseDouble(stockInfo.get("stockWt"))  + Double.parseDouble(detailMap.get("realDlvrWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt" , String.valueOf(stockWt));
				}
			}			
			
			// 매입 N 이면서  P 매입확정, A 일괄인 경우 매입확정 및 재고증가
			if("N".equals(paramMap.get("ordrgYn")) && ("P".equals(paramMap.get("comfirmType"))|| "A".equals(paramMap.get("comfirmType")))) {
				// 발주 상세에 확정 Flag = 'Y 표기
				od01Mapper.updateConfirmDetail(detailMap);
				
				//매입, 매입금액이 없는 경우 매입내역 등록 안함.
				/*double bilgAmtPchs =  Double.parseDouble(detailMap.get("realDlvrAmt"));
				if (bilgAmtPchs > 0 || bilgAmtPchs < 0) {*/
					paramMap.put("clntCd", clntCd);
					
					// 매입실적 반영 AR02
			    	ar02Mapper.insertPchsSell(paramMap);
			    	

			    	if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
					{
			    		// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
						if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
							paramMap.put("clntCd",  paramMap.get("whClntCd"));		
						}
						// 재고 반영
						sm01Mapper.updateStockSell(paramMap);
						
						// MES 입고 전송, 단 제강사 턴키 일 경우에 한함.	
				    	if("Y".equals(paramMap.get("tnkeyYn"))){
				    		insertIfMesStockIn(paramMap); 
						}
					}
				/* } */
			}	

			// S 매출확정, A 일괄
			// 직송일 이면서 매출확정 혹은 일괄확정일 경우 매출 생성 및 재고차감.
			// 매출만 단독으로 확정가능함. - 20210611
			
	        // 매출확정 시작
			if ("Y".equals(paramMap.get("dirtrsYn")) && "N".equals(paramMap.get("shipYn"))) {
				if ("A".equals(paramMap.get("comfirmType")) || ("S".equals(paramMap.get("comfirmType")))) {
				// 전체 확정, 매출확정
					paramMap.put("selpchCd", "SELPCH2");
					paramMap.put("stockChgCd", "STOCKCHG02");
					paramMap.put("clntCd", sellClntCd);
					paramMap.put("clntNm", sellClntNm);
					paramMap.put("sellUpr", detailMap.get("shipUpr"));
					paramMap.put("trspRmk", paramMap.get("dlvrRmk"));

					// 매출자료를 세팅한다... 매출단가를 기준으로 모든 금액을 재계산한다.
					paramMap.put("trstUpr", detailMap.get("shipUpr"));
					paramMap.put("realTrstUpr", detailMap.get("shipUpr"));
					paramMap.put("bilgUpr", detailMap.get("shipUpr"));

					// 재고관리 대상이 아닌 매출의 경우 물량 부문이 0으로 정리됨.
					if (detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) {
						BigDecimal trstQty = new BigDecimal(detailMap.get("ordrgQty"));
						BigDecimal realDlvrQty = new BigDecimal(detailMap.get("realDlvrQty"));
						BigDecimal shipUpr = new BigDecimal(detailMap.get("shipUpr"));
						
						int trstAmt = (int) Math.floor(shipUpr.multiply(trstQty).doubleValue());
						int realTrstAmt = (int) Math.floor(shipUpr.multiply(realDlvrQty).doubleValue());
						int bilgAmt = (int) Math.floor(shipUpr.multiply(realDlvrQty).doubleValue());
						
						paramMap.put("trstAmt", String.valueOf(trstAmt));
						paramMap.put("realTrstAmt", String.valueOf(realTrstAmt));
						paramMap.put("bilgAmt", String.valueOf(bilgAmt));
					} else {
						paramMap.put("trstAmt", detailMap.get("shipAmt"));
						paramMap.put("realTrstAmt", detailMap.get("shipAmt"));
						paramMap.put("bilgAmt", detailMap.get("shipAmt"));
					}

					long bilgVatAmt2 = ar02Mapper.selectBilgVatAmt(paramMap);
					paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt2));
                   // 발주 상세에 
					od01Mapper.updateConfirmDetailS(detailMap);
					ar02Mapper.insertPchsSell(paramMap);

					if (detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) {
						// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
						if ("OWNER1".equals(paramMap.get("ownerCd").toString())) {
							paramMap.put("clntCd", paramMap.get("whClntCd"));
						}
						Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
					if(stockInfo == null) {
						paramMap.put("stockQty", "-"+detailMap.get("realDlvrQty"));
						paramMap.put("stockWt",  "-"+detailMap.get("realDlvrWt"));
					} else {
						double stockQty = Double.parseDouble(stockInfo.get("stockQty")) - Double.parseDouble(detailMap.get("realDlvrQty"));
						double stockWt = Double.parseDouble(stockInfo.get("stockWt")) - Double.parseDouble(detailMap.get("realDlvrWt"));
						paramMap.put("stockQty", String.valueOf(stockQty));
						paramMap.put("stockWt", String.valueOf(stockWt));
						paramMap.put("sellUpr", detailMap.get("shipUpr"));
					}			
						sm01Mapper.updateStockSell(paramMap);
					}
				}
			}
		} // for문 종료
		
		if(od01Mapper.selectConfirmCount(paramMap) == od01Mapper.selectDetailCount(paramMap)) {
			od01Mapper.updateConfirm(paramMap);
		}
		
		if(od01Mapper.selectConfirmCountS(paramMap) == od01Mapper.selectDetailCount(paramMap)) {
			od01Mapper.updateConfirmS(paramMap);
		}
		
		// 직송이면서 매출여부 N인경우 매출확정 시작
        if("Y".equals(paramMap.get("dirtrsYn"))) {		
		       // 전체 확정인 경우는 진행
		       // 매출확정이면서 매입이 확정이 된경우 P 매입확정, S 매출확정, A 일괄
	       	if ( "A".equals(paramMap.get("comfirmType")) || "S".equals(paramMap.get("comfirmType"))) {
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
        }
	}

	@Override
	public void updateCancel(Map<String, String> paramMap) throws Exception{
		// P 매입취소, S 매출취소, A 일괄 취소 
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		String sellClntCd = paramMap.get("sellClntCd");
		
		for(Map<String, String> detailMap : detailList) {
			// detailMap set
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.putAll(od01Mapper.selectOrdrgDetailInfo(detailMap));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			detailMap.put("trstRprcSeq", detailMap.get("ordrgSeq"));
			detailMap.put("trstDtlSeq", detailMap.get("ordrgDtlSeq"));
			
			if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
				if("Y".equals(detailMap.get("ordrgYn"))){
					// 매입마감 체크
					if(!ar02Svc.checkPchsClose(paramMap)) {
						thrower.throwCommonException("pchsClose");
					}
				}else{
					// 이미 매입취소된 상세내역이면 매입취소 불가능
					thrower.throwCommonException("alreadyCancel");
				}
			}
			// 직송이면서 매출, 일괄의 경우
			if(("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))) &&"Y".equals(paramMap.get("dirtrsYn"))){
				if("Y".equals(detailMap.get("shipYn"))){
					// 매출마감 체크
					if(!ar02Svc.checkSellClose(paramMap)) {
	            		thrower.throwCommonException("sellClose");
	        		}			
				}else {
					// 이미 매출취소된 상세내역이면 매출취소 불가능
					thrower.throwCommonException("alreadyCancel");
				}
			}
			
			// 매입확정 체크
			if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
			    od01Mapper.updateCancelDetail(detailMap);
			    detailMap.put("selpchCd", "SELPCH1");
				List<Map<String, String>> bilgList = ar02Mapper.checkBilg(detailMap);
				for (Map<String, String> map : bilgList) {
					if(map != null && Integer.parseInt(map.get("bilgCertNo")) != 0) {
						// 전표처리 되었으면 취소 불가 : rollback
						thrower.throwCommonException("bilgComplete");
					}
				}				
				ar02Mapper.deletePchsSell(detailMap); // 매입만 삭제
			}
			
			//직송이면서 매출확정 체크
			if(("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))&&"Y".equals(paramMap.get("dirtrsYn"))){
			    od01Mapper.updateCancelDetailS(detailMap);
			    double testValue = 10 / 0;
			    detailMap.put("selpchCd", "SELPCH2");
				List<Map<String, String>> bilgList = ar02Mapper.checkBilg(detailMap);
				for (Map<String, String> map : bilgList) {
					if(map != null && Integer.parseInt(map.get("bilgCertNo")) != 0) {
						// 전표처리 되었으면 취소 불가 : rollback
						thrower.throwCommonException("bilgComplete");
					}
				}
				ar02Mapper.deletePchsSell(detailMap);  // 매출만 삭제
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
				
				Map<String, String> stockInfo = null;
				double stockQty = 0;
				double stockWt  = 0;
				
				// 매입이 Y && (P 매입취소 || A 일괄 취소)
				if("Y".equals(detailMap.get("ordrgYn")) && ("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))){
					stockInfo = sm01Mapper.selectStockInfo(paramMap);
					if(stockInfo == null) {
						stockQty =  -1* Double.parseDouble(detailMap.get("realDlvrQty"));
						stockWt  =  -1* Double.parseDouble(detailMap.get("realDlvrWt"));
						
					}else {
						
						stockQty = Double.parseDouble(stockInfo.get("stockQty")) - Double.parseDouble(detailMap.get("realDlvrQty"));
						stockWt  = Double.parseDouble(stockInfo.get("stockWt"))  - Double.parseDouble(detailMap.get("realDlvrWt"));
					}
					paramMap.put("stockQty", String.valueOf(stockQty));					
					paramMap.put("stockWt" , String.valueOf(stockWt));					
				    sm01Mapper.updateStockCancel(paramMap);
				}
				
				// 직송이 Y && 매출이 Y (매출취소(S) || 일괄취소(A))
				if("Y".equals(paramMap.get("dirtrsYn")) && "Y".equals(detailMap.get("shipYn")) &&
				  ("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))) {
					stockInfo = sm01Mapper.selectStockInfo(paramMap);
					if(stockInfo == null) {
						stockQty = Double.parseDouble(detailMap.get("realDlvrQty"));
						stockWt  = Double.parseDouble(detailMap.get("realDlvrWt"));
						
					}else {
						
						stockQty = Double.parseDouble(stockInfo.get("stockQty")) + Double.parseDouble(detailMap.get("realDlvrQty"));
						stockWt  = Double.parseDouble(stockInfo.get("stockWt"))  + Double.parseDouble(detailMap.get("realDlvrWt"));
					}
					
					
					
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt" , String.valueOf(stockWt));
					sm01Mapper.updateStockCancel(paramMap);
				}
			}	
		}

		// P 매입취소, A 일괄 취소
		if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
			od01Mapper.updateCancel(paramMap);
		}
		
		// 직송이 Y && (매출취소(P) || 일괄취소(A))
		if("Y".equals(paramMap.get("dirtrsYn")) && ("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))) {
			od01Mapper.updateCancelS(paramMap);
			
			/* 그룹별 여신 리스트 생성 start */
			List<Map<String, Object>> loanList = new ArrayList<Map<String,Object>>();
			
			// 그룹별 금액 Map 
			Map<String, Object> grpAmtMap = new LinkedHashMap<String, Object>();
			for(Map<String, String> detailMap : detailList) {
				String prdtGrp = bm01Mapper.selectProductGroup(detailMap.get("prdtCd"));
				if(grpAmtMap.containsKey(prdtGrp)) {
					grpAmtMap.put(prdtGrp, (Long) grpAmtMap.get(prdtGrp) + Long.parseLong(detailMap.get("shipAmt")));
				}else {
					grpAmtMap.put(prdtGrp, Long.parseLong(detailMap.get("shipAmt")));
				}
			}
			
			// 여신 Map
    		Map<String, String> bilgVatPerMap = new HashMap<String, String>();
    		bilgVatPerMap.put("selpchCd", "SELPCH2");
    		bilgVatPerMap.put("clntCd", sellClntCd);
			int bilgVatPer = ar02Mapper.selectBilgVatPer(bilgVatPerMap);
			for(Map.Entry<String, Object> entry : grpAmtMap.entrySet()) {
				String prdtGrp = entry.getKey();
				Long totAmt = (Long) entry.getValue();
				Map<String, Object> loanMap = new HashMap<String, Object>();
				loanMap.put("coCd", paramMap.get("coCd"));
				loanMap.put("clntCd", sellClntCd);
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
	}
	
	public void insertIfMesStockIn(Map<String, String> paramMap) throws Exception{
	// 제강사 턴키의 경우 입고 이력을 MES에 전송한다.
//	   	WH01	진천공장   J
//    	WH05	인천공장   N
//    	WH06	창녕공장   C
		
//		WH06	창녕공장	    WH	GGS	WHDIV01	ESTCOPRT5
//		WH13	지지스틸창녕공장	WH	GUM	WHDIV03	ESTCOPRT1
//		WH29	지지엠창녕	    WH	GGM	WHDIV02	ESTCOPRT4
//		WH32	지지스틸창녕공장	WH	GGM	WHDIV03	ESTCOPRT4
		
//		WH05	인천공장	    WH	GUM	WHDIV01	ESTCOPRT1
//		WH20	금문인천공장	WH	GGM	WHDIV03	ESTCOPRT4
		
//		WH01	진천공장	    WH	GUM	WHDIV01	ESTCOPRT1
//		WH19	금문진천공장	WH	GGM	WHDIV03	ESTCOPRT4
//		WH41	진천공장2	    WH	GUM	WHDIV01	ESTCOPRT1
//		
		paramMap.put("worksCd", "");
    	if  ("WH01".equals(paramMap.get("whCd")) ||"WH19".equals(paramMap.get("whCd")) ||"WH41".equals(paramMap.get("whCd"))) {
    		paramMap.put("worksCd", "J");
    	}else if("WH05".equals(paramMap.get("whcd")) ||"WH20".equals(paramMap.get("whCd")) ) {
    		paramMap.put("worksCd", "N"); 
    	}else if("WH06".equals(paramMap.get("whcd")) ||"WH13".equals(paramMap.get("whCd")) ||"WH29".equals(paramMap.get("whCd")) ||"WH32".equals(paramMap.get("whCd"))) {
    		paramMap.put("worksCd", "C"); 
    	}
    	
    	if("Y".equals(paramMap.get("prdtCoilYn"))) {
    		    paramMap.put("productNameCd", "BC");    		    
    	}else { paramMap.put("productNameCd", "BD");
    	}
    	
    	/* 2) SET sPrdtSpec 마지막 문자 C 제거 후 삽입 */   	
    	String spec = paramMap.get("prdtSpec");    	
    	paramMap.put("dimsCd", paramMap.get("prdtSpec")); 
    	if (spec != null && spec.length() > 0 && spec.charAt(spec.length() - 1) == 'C') { 
    		paramMap.put("dimsCd", spec.substring(0, spec.length() - 1)); 
    	}    			
        
    	paramMap.put("moveWt",paramMap.get("realTrstWt")) ;
          
    	mesStockMapper.insertIfMesStockIn(paramMap); 	

	}
}