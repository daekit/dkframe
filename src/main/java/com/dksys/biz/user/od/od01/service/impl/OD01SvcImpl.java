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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.od.od01.mapper.OD01Mapper;
import com.dksys.biz.user.od.od01.service.OD01Svc;
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
    CM08Svc cm08Svc;
    
	@Override
	public int insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
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
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od01Mapper.insertOrderDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("reqDt")+paramMap.get("ordrgSeq"), mRequest);
		return result;
	}

	@Override
	public int selectOrderCount(Map<String, String> paramMap) {
		return od01Mapper.selectOrderCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectOrderList(Map<String, String> paramMap) {
		return od01Mapper.selectOrderList(paramMap);
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
		returnMap.put("orderDetail", od01Mapper.selectOrderDetail(paramMap));
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
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		result = detailList.size();
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od01Mapper.updateConfirmDetail(detailMap);
			detailMap = od01Mapper.selectOrderDetailInfo(detailMap);
			paramMap.putAll(detailMap);
			paramMap.put("selpchCd", "SELPCH1");
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
			ar02Mapper.insertPchsSell(paramMap);
			// 재고정보 update
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			paramMap.put("stockChgCd", "STOCKCHG02");
			if(stockInfo == null) {
				paramMap.put("pchsUpr", detailMap.get("realDlvrUpr"));
				paramMap.put("sellUpr", detailMap.get("realDlvrUpr"));
				paramMap.put("stockUpr", detailMap.get("realDlvrUpr"));
				paramMap.put("stdUpr", detailMap.get("realDlvrUpr"));
				paramMap.put("stockQty", detailMap.get("realDlvrQty"));
			} else {
				paramMap.put("pchsUpr", detailMap.get("pchsUpr"));
				paramMap.put("sellUpr", detailMap.get("sellUpr"));
				paramMap.put("stockUpr", detailMap.get("stockUpr"));
				paramMap.put("stdUpr", detailMap.get("realDlvrUpr"));
				int stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(detailMap.get("realDlvrQty"));
				paramMap.put("stockQty", String.valueOf(stockQty));
			}
			sm01Mapper.updateStockSell(paramMap);
			if("Y".equals(paramMap.get("dirtrsYn"))) {
				paramMap.put("selpchCd", "SELPCH2");
				stockInfo = sm01Mapper.selectStockInfo(paramMap);
				int stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realDlvrQty"));
				paramMap.put("stockQty", String.valueOf(stockQty));
				ar02Mapper.insertPchsSell(paramMap);
				sm01Mapper.updateStockSell(paramMap);
			}
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