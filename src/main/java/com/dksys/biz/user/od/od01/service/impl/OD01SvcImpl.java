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
		if("".equals(paramMap.get("odrSeq"))) {
			isOdr = true;
			paramMap.put("totQty", paramMap.get("totQty"));
			paramMap.put("totWt", paramMap.get("totQty"));
			paramMap.put("totAmt", paramMap.get("totAmt"));
			paramMap.put("odrRmk", paramMap.get("ordrgRmk"));
			sd04Mapper.insertOrder(paramMap);
		}
		int result = od01Mapper.insertOrder(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		od01Mapper.deleteOrderDetail(paramMap);
		// 발주상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
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
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
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
		}
		if(isOdr) {
			cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
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
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od01Mapper.insertOrderDetail(detailMap);
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
		int realTotTrstAmt = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		result = detailList.size();
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			//커플러일 경우 별도 단가 데이터 저장
			if(detailMap.get("prdtStkn").contains("PDSKCP")) {
				detailMap.put("coCd", paramMap.get("coCd"));
				detailMap.put("clntCd", paramMap.get("clntCd"));
				detailMap.put("selpchCd", "SELPCH1");
				detailMap.put("rmk", paramMap.get("ordrgDtlRmk"));
				sd08Mapper.insertCplrUntpc(detailMap);
				if("Y".equals(paramMap.get("dirtrsYn"))) {
					detailMap.put("selpchCd", "SELPCH2");
					detailMap.put("clntCd", paramMap.get("sellClntCd"));
					sd08Mapper.insertCplrUntpc(detailMap);
				}
			}
			od01Mapper.updateConfirmDetail(detailMap);
			detailMap = od01Mapper.selectOrderDetailInfo(detailMap);
			paramMap.putAll(detailMap);
			paramMap.put("selpchCd", "SELPCH1");
			paramMap.put("trstDt", paramMap.get("dlvrDttm").replace("-", ""));
			paramMap.put("estCoprt", detailMap.get("taxivcCoprt"));
			paramMap.put("pchsUpr", detailMap.get("realDlvrUpr"));
			paramMap.put("stockUpr", detailMap.get("stockUpr"));
			paramMap.put("trstQty", detailMap.get("ordrgQty"));
			paramMap.put("trstWt", detailMap.get("ordrgWt"));
			paramMap.put("trstUpr", detailMap.get("ordrgUpr"));
			paramMap.put("trstAmt", detailMap.get("ordrgAmt"));
			paramMap.put("realTrstQty", detailMap.get("realDlvrQty"));
			paramMap.put("realTrstWt", detailMap.get("realDlvrWt"));
			paramMap.put("realTrstUpr", detailMap.get("realDlvrUpr"));
			paramMap.put("realTrstAmt", detailMap.get("realDlvrAmt"));
			paramMap.put("bilgQty", detailMap.get("realDlvrQty"));
			paramMap.put("bilgWt", detailMap.get("realDlvrWt"));
			paramMap.put("bilgUpr", detailMap.get("realDlvrUpr"));
			paramMap.put("bilgAmt", detailMap.get("realDlvrAmt"));
			paramMap.put("clntNm", detailMap.get("clntNm"));
			realTotTrstAmt += Integer.parseInt(paramMap.get("realTrstAmt"));
			ar02Mapper.insertPchsSell(paramMap);
			if(detailMap.containsKey("prdtStockCd") && "Y".equals(detailMap.get("prdtStockCd").toString())) 
			{
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
				}
				paramMap.put("prdtCd", detailMap.get("prdtCd"));
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				paramMap.put("stockChgCd", "STOCKCHG01");
				if(stockInfo == null) {
					paramMap.put("pchsUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("sellUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("stockUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("stdUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("stockQty", detailMap.get("realDlvrQty"));
				} else {
					paramMap.put("pchsUpr", detailMap.get("realDlvrUpr"));
					paramMap.put("sellUpr", stockInfo.get("sellUpr"));
					paramMap.put("stockUpr", stockInfo.get("stockUpr"));
					paramMap.put("stdUpr", stockInfo.get("stdUpr"));
					int stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(detailMap.get("realDlvrQty"));
					paramMap.put("stockQty", String.valueOf(stockQty));
				}
				sm01Mapper.updateStockSell(paramMap);
			}
			if("Y".equals(paramMap.get("dirtrsYn"))) {
				paramMap.put("selpchCd", "SELPCH2");
				paramMap.put("stockChgCd", "STOCKCHG02");
				paramMap.put("cnltCd", paramMap.get("sellClntCd"));
				paramMap.put("cnltNm", paramMap.get("sellClntNm"));
				Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
				paramMap.put("sellUpr", detailMap.get("realDlvrUpr"));
				int stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realDlvrQty"));
				paramMap.put("stockQty", String.valueOf(stockQty));
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
		paramMap.put("realTotTrstAmt", String.valueOf(realTotTrstAmt));
		if(ar02Svc.checkLoan(paramMap)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		if(selectConfirmCount(paramMap) == selectDetailCount(paramMap)) {
			od01Mapper.updateConfirm(paramMap);
		}
		return result;
	}

	public int selectDetailCount(Map<String, String> paramMap) {
		return od01Mapper.selectDetailCount(paramMap);
	}

	public int selectConfirmCount(Map<String, String> paramMap) {
		return od01Mapper.selectConfirmCount(paramMap);
	}

}