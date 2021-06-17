package com.dksys.biz.user.od.od01.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.od.od01.mapper.OD01Mapper;
import com.dksys.biz.user.od.od01.service.OD01Svc;
import com.dksys.biz.user.od.od04.mapper.OD04Mapper;
import com.dksys.biz.user.sd.sd04.mapper.SD04Mapper;
import com.dksys.biz.user.sd.sd08.mapper.SD08Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
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
    AR02Svc ar02Svc;
    
    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public int insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
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
		int result = od01Mapper.insertOrder(paramMap);
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
				detailMap.put("odrDtlRmk", detailMap.get("ordrgDtlRmk"));
				sd04Mapper.insertOrderDetail(detailMap);
			}
			od01Mapper.insertOrderDetail(detailMap);
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
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("reqDt")+paramMap.get("ordrgSeq"), mRequest);
		return result;
	}

	@Override
	public int selectOrderCount(Map<String, String> paramMap) {
		return od01Mapper.selectOrderCount(paramMap);
	}
	
	@Override
	public int selectOrderDetailCount(Map<String, String> paramMap) {
		return od01Mapper.selectOrderDetailCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectOrderList(Map<String, String> paramMap) {
		return od01Mapper.selectOrderList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectOrderDetailList(Map<String, String> paramMap) {
		return od01Mapper.selectOrderDetailList(paramMap);
	}

	@Override
	public int deleteOrder(Map<String, String> paramMap) {
		int result = od01Mapper.deleteOrder(paramMap);
		result += od01Mapper.deleteOrderDetail(paramMap);
		return result;
	}

	@Override
	public Map<String, Object> selectOrderInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("reqDt")+paramMap.get("ordrgSeq")));
		returnMap.put("orderInfo", od01Mapper.selectOrderInfo(paramMap));
		returnMap.put("orderDetail", od01Mapper.selectOrderDetailList(paramMap));
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getOrderInfo(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("orderInfo", od01Mapper.getOrderInfo(paramMap));
		returnMap.put("orderDetail", od01Mapper.getOrderDetailList(paramMap));
		return returnMap;
	}

	@Override
	public int updateOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = od01Mapper.updateOrder(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		od01Mapper.deleteOrderDetail(paramMap);
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
				od01Mapper.updateOrderDetail(detailMap);
			}else {
				od01Mapper.insertOrderDetail(detailMap);
			}			
		}
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("reqDt")+paramMap.get("ordrgSeq"), mRequest);
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		return result;
	}

	@Override
	public int updateConfirm(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		result = detailList.size();
		
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
				return 500;
			}	
		}		
		
		// 직송이면서 매출여부 N인경우 매출확정 시작
         if("Y".equals(paramMap.get("dirtrsYn"))) {		
		       // 전체 확정인 경우는 진행
		       // 매출확정이면서 매입이 확정이 된경우 P 매입확정, S 매출확정, A 일괄
        	if ( "A".equals(paramMap.get("comfirmType")) || "S".equals(paramMap.get("comfirmType"))) {
				// 마감 체크 (매출)
        		if(!ar02Svc.checkSellClose(paramMap)) {
        			return 501;
				}
        		
        		// 여신 체크 (매출)
        		// 1. 매출금액합계 계산
        		long totShipAmt = 0;
        		for(Map<String, String> detailMap : detailList) {
        			if("N".equals(detailMap.get("shipYn"))) {
        				totShipAmt += Long.parseLong(detailMap.get("shipAmt"));
        			}
        		}
        		
        		// 2. 부가세율 조회해서 계산후 플러스
        		Map<String, String> vatPerMap = new HashMap<String, String>();
        		vatPerMap.put("selpchCd", "SELPCH2");
        		vatPerMap.put("clntCd", paramMap.get("sellClntCd"));
        		
        		int bilgVatPer = ar02Mapper.selectBilgVatPer(vatPerMap);
        		totShipAmt += Math.floor(totShipAmt * bilgVatPer / 100);
        		
        		// 3. 여신체크
        		Map<String, String> loanMap = new HashMap<String, String>();
        		loanMap.put("clntCd", paramMap.get("sellClntCd"));
        		loanMap.put("coCd", paramMap.get("coCd"));
        		loanMap.put("dlvrDttm", paramMap.get("dlvrDttm"));
        		loanMap.put("realTotTrstAmt", String.valueOf(totShipAmt));
        		if (ar02Svc.checkLoan(loanMap)) {
        			paramMap.put("diffLoan", loanMap.get("diffLoan"));
					return 0;
				}
        	}
        }
//-----------------------------------------------------------------------------------------------------------------------------------------------		
		String clntCd = paramMap.get("clntCd");
		String clntNm = paramMap.get("clntNm");
		String sellClntCd = paramMap.get("sellClntCd");
		String sellClntNm = paramMap.get("sellClntNm");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
            detailMap.put("reqSeq", paramMap.get("reqSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			//커플러일 경우 별도 단가 데이터 저장
			if(detailMap.get("prdtStkn").contains("PDSKCP")) {
				detailMap.put("coCd", paramMap.get("coCd"));
				detailMap.put("clntCd", clntCd);
				detailMap.put("selpchCd", "SELPCH1");
				detailMap.put("prdtDt", paramMap.get("reqDt"));
				detailMap.put("prdtUpr", detailMap.get("realDlvrUpr"));
				detailMap.put("rmk", paramMap.get("ordrgDtlRmk"));
				sd08Mapper.insertCplrUntpc(detailMap);
				if("Y".equals(paramMap.get("dirtrsYn"))) {
					detailMap.put("selpchCd", "SELPCH2");
					detailMap.put("clntCd", sellClntCd);
					sd08Mapper.insertCplrUntpc(detailMap);
				}
			}

			//매출매입 데이터 세팅
			Map<String, String> detailMap2 = od01Mapper.selectOrderDetailInfo(detailMap);

			paramMap.put("prdtSpec",    "");
			paramMap.put("prdtSize",    "");
			paramMap.put("prdtLen",    "");
			
			paramMap.putAll(detailMap2);
			detailMap.putAll(detailMap2);
			
			paramMap.put("selpchCd",    "SELPCH1");
			paramMap.put("trstDt",      paramMap.get("dlvrDttm").replace("-", ""));
			paramMap.put("pchsUpr",     detailMap2.get("realDlvrUpr"));
			paramMap.put("stockUpr",    detailMap2.get("stockUpr"));
			paramMap.put("trstQty",     detailMap2.get("ordrgQty"));
			paramMap.put("trstWt",      detailMap2.get("ordrgWt"));
			paramMap.put("trstUpr",     detailMap2.get("ordrgUpr"));
			paramMap.put("trstAmt",     detailMap2.get("ordrgAmt"));
			paramMap.put("realTrstQty", detailMap2.get("realDlvrQty"));
			paramMap.put("realTrstWt",  detailMap2.get("realDlvrWt"));
			paramMap.put("realTrstUpr", detailMap2.get("realDlvrUpr"));
			paramMap.put("realTrstAmt", detailMap2.get("realDlvrAmt"));
			paramMap.put("bilgQty",     detailMap2.get("realDlvrQty"));
			paramMap.put("bilgWt",      detailMap2.get("realDlvrWt"));
			paramMap.put("bilgUpr",     detailMap2.get("realDlvrUpr"));
			paramMap.put("bilgAmt",     detailMap2.get("realDlvrAmt"));
			paramMap.put("clntNm",      detailMap2.get("clntNm"));
			paramMap.put("trstRprcSeq", detailMap2.get("ordrgSeq"));		
			paramMap.put("trstDtlSeq",  detailMap2.get("ordrgDtlSeq"));				  	
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
					int stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(detailMap.get("realDlvrQty"));
					int stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  + Integer.parseInt(detailMap.get("realDlvrWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt" , String.valueOf(stockWt));
				}
			}			
			
			// 매입 N 이면서  P 매입확정, A 일괄인 경우 매입확정 및 재고증가
			if("N".equals(paramMap.get("ordrgYn")) && ("P".equals(paramMap.get("comfirmType"))|| "A".equals(paramMap.get("comfirmType")))) {
				
				od01Mapper.updateConfirmDetail(detailMap);
				//매입, 매입금액이 없는 경우 매입내역 등록 안함.
				double bilgAmtPchs     =  Double.parseDouble(detailMap.get("realDlvrAmt"));
				if (bilgAmtPchs > 0 || bilgAmtPchs < 0) {
					paramMap.put("clntCd", clntCd);
			    	ar02Mapper.insertPchsSell(paramMap);
			    	if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
					{
			    		// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
						if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
							paramMap.put("clntCd",  paramMap.get("whClntCd"));		
						}
						sm01Mapper.updateStockSell(paramMap);
					}
				}
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

					// 매출자료를 세팅한다... 매출단가를 기준으로 모든 금액을 재계산한다.
					paramMap.put("trstUpr", detailMap.get("shipUpr"));
					paramMap.put("realTrstUpr", detailMap.get("shipUpr"));
					paramMap.put("bilgUpr", detailMap.get("shipUpr"));

					// 재고돤리 대상이 아닌 매출의 경우 물량 부문이 0으로 정리됨.
					if (detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) {
						double trstQty = Double.parseDouble(detailMap.get("ordrgQty"));
						double realDlvrQty = Double.parseDouble(detailMap.get("realDlvrQty"));
						double shipUpr = Double.parseDouble(detailMap.get("shipUpr"));

						int trstAmt = (int) Math.floor(shipUpr * trstQty);
						int realTrstAmt = (int) Math.floor(shipUpr * realDlvrQty);
						int bilgAmt = (int) Math.floor(shipUpr * realDlvrQty);
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
						int stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realDlvrQty"));
						int stockWt = Integer.parseInt(stockInfo.get("stockWt")) - Integer.parseInt(detailMap.get("realDlvrWt"));
						paramMap.put("stockQty", String.valueOf(stockQty));
						paramMap.put("stockWt", String.valueOf(stockWt));
						paramMap.put("sellUpr", detailMap.get("shipUpr"));
					}			
						sm01Mapper.updateStockSell(paramMap);
					}
				}
			}
		} // for문 종료
		
		if(selectConfirmCount(paramMap) == selectDetailCount(paramMap)) {
			od01Mapper.updateConfirm(paramMap);
		}
		
		if(selectConfirmCountS(paramMap) == selectDetailCount(paramMap)) {
			od01Mapper.updateConfirmS(paramMap);
		}
		
		return result;
	}

	public int selectDetailCount(Map<String, String> paramMap) {
		return od01Mapper.selectDetailCount(paramMap);
	}

	public int selectConfirmCount(Map<String, String> paramMap) {
		return od01Mapper.selectConfirmCount(paramMap);
	}
	public int selectConfirmCountS(Map<String, String> paramMap) {
		return od01Mapper.selectConfirmCountS(paramMap);
	}
	@Override
	public int updateCancel(Map<String, String> paramMap) {

		// 작업 시작전에 매입은 체크 매출은 건별로 체크
		if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
			// 마감 체크....매입
			if(!ar02Svc.checkPchsClose(paramMap)) {
				return 500;
			}				
		}
		
		int result = 0;
		long totShipAmt = 0;
		boolean bilgFlag = false;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		result = detailList.size();
		String sellClntCd = paramMap.get("sellClntCd");
		
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			detailMap.put("trstRprcSeq", paramMap.get("ordrgSeq"));
			detailMap.put("trstDtlSeq", detailMap.get("ordrgDtlSeq"));
			totShipAmt += Integer.parseInt(detailMap.get("shipAmt"));

			if("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
				
				if("Y".equals(detailMap.get("shipYn"))){
					//마감 체크 .. 매출   ,   매출없이 매입만 있는 경우 매출에 대한 마감이 진행 중이면.. 매입도 마감이 아니어도 취소가 안됨.해서 매출 건별로 먼저 체크함.
					if(!ar02Svc.checkSellClose(paramMap)) {
						return 501;
					}				
				}
			    od01Mapper.updateCancelDetailS(detailMap);
			    detailMap.put("selpchCd", "SELPCH2");
			    //매출확정 체크
				List<Map<String, String>> bilgList = ar02Mapper.checkBilg(detailMap);
				for (Map<String, String> map : bilgList) {
					if(map != null && Integer.parseInt(map.get("bilgCertNo")) != 0) {
						bilgFlag = true;
						break;
					}
				}
				ar02Mapper.deletePchsSell(detailMap);  //   SELPCH2	 매출만 삭제
			} 
			
			// P 매입취소, S 매출취소, A 일괄 취소 
			if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
			    od01Mapper.updateCancelDetail(detailMap);
			    detailMap.put("selpchCd", "SELPCH1");
			    // 매입확정 체크
				List<Map<String, String>> bilgList = ar02Mapper.checkBilg(detailMap);
				for (Map<String, String> map : bilgList) {
					if(map != null && Integer.parseInt(map.get("bilgCertNo")) != 0) {
						bilgFlag = true;
						break;
					}
				}				
				ar02Mapper.deletePchsSell(detailMap); //   	 메입만 삭제
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
				
				Map<String, String> stockInfo =null;
				int stockQty = 0;
				int stockWt  = 0;
				
				// P 매입취소, A 일괄 취소 : 매입이 Y 인경우
				if("Y".equals(detailMap.get("ordrgYn")) && ("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))){
				
					stockInfo = sm01Mapper.selectStockInfo(paramMap);
					stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realDlvrQty"));
					stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  - Integer.parseInt(detailMap.get("realDlvrWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));					
					paramMap.put("stockWt" , String.valueOf(stockWt));					
				     sm01Mapper.updateStockCancel(paramMap);
				}
				
				// 직송이면서 매출취소(S), 일괄취소(A)  : 매출이 Y 인경우
				if("Y".equals(paramMap.get("dirtrsYn")) && "Y".equals(detailMap.get("shipYn")) &&
				  ("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))) {
					stockInfo = sm01Mapper.selectStockInfo(paramMap);
					stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(detailMap.get("realDlvrQty"));
					stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  + Integer.parseInt(detailMap.get("realDlvrWt"));
					paramMap.put("stockQty", String.valueOf(stockQty));
					paramMap.put("stockWt" , String.valueOf(stockWt));
					sm01Mapper.updateStockCancel(paramMap);
				}
			}	
		}
		if(bilgFlag) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}

		// P 매입취소, A 일괄 취소
		if("P".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType"))){
		od01Mapper.updateCancel(paramMap);
		}
		// 직송이면서 매출취소(P), 일괄취소(A)  반영
		if("Y".equals(paramMap.get("dirtrsYn")) && ("S".equals(paramMap.get("cancelType")) || "A".equals(paramMap.get("cancelType")))) {
			od01Mapper.updateCancelS(paramMap);
			
			// 2. 부가세율 조회해서 계산후 플러스
    		Map<String, String> vatPerMap = new HashMap<String, String>();
    		vatPerMap.put("selpchCd", "SELPCH2");
    		vatPerMap.put("clntCd", sellClntCd);
    		int bilgVatPer = ar02Mapper.selectBilgVatPer(vatPerMap);
    		totShipAmt += Math.floor(totShipAmt * bilgVatPer / 100);
			
			// 여신 원복
			paramMap.put("clntCd",  sellClntCd);
			paramMap.put("creditAmt", String.valueOf(totShipAmt));
			Map<String, Object> paramMapObj = new HashMap<>(paramMap);
			ar02Svc.creditDeposit(paramMapObj);
			
		}
		return result;
	}
	
}