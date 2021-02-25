package com.dksys.biz.user.ar.ar01.service.impl;

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
import com.dksys.biz.user.ar.ar01.mapper.AR01Mapper;
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class AR01SvcImpl implements AR01Svc {
    
    @Autowired
    AR01Mapper ar01Mapper;
    
    @Autowired
    AR02Mapper ar02Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Autowired
    AR01Svc ar01Svc;

    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public int insertShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = ar01Mapper.insertShip(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		ar01Mapper.deleteShipDetail(paramMap);
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
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("odrDtlSeq", paramMap.get("odrDtlSeq"));
			detailMap.put("ordrgDtlSeq", paramMap.get("ordrgDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.insertShipDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_AR01M01", paramMap.get("reqDt")+paramMap.get("shipSeq"), mRequest);
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
		int result = ar01Mapper.deleteShip(paramMap);
		result += ar01Mapper.deleteShipDetail(paramMap);
		return result;
	}

	@Override
	public Map<String, Object> selectShipInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("reqDt")+paramMap.get("shipSeq")));
		returnMap.put("shipInfo", ar01Mapper.selectShipInfo(paramMap));
		returnMap.put("shipDetail", ar01Mapper.selectShipDetail(paramMap));
		return returnMap;
	}

	@Override
	public int updateShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = ar01Mapper.updateShip(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		ar01Mapper.deleteShipDetail(paramMap);
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
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("odrDtlSeq", paramMap.get("odrDtlSeq"));
			detailMap.put("ordrgDtlSeq", paramMap.get("ordrgDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.insertShipDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_AR01M01", paramMap.get("reqDt")+paramMap.get("shipSeq"), mRequest);
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
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("odrDtlSeq", paramMap.get("odrDtlSeq"));
			detailMap.put("ordrgDtlSeq", paramMap.get("ordrgDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.updateConfirmDetail(detailMap);
			//매출정보 insert
			detailMap = ar01Mapper.selectShipDetailInfo(detailMap);
			paramMap.putAll(detailMap);
			paramMap.put("selpchCd", "SELPCH2");
			paramMap.put("pchsUpr", detailMap.get("pchsUpr"));
			paramMap.put("sellUpr", detailMap.get("realShipUpr"));
			paramMap.put("stockUpr", detailMap.get("stockUpr"));
			paramMap.put("trstQty", detailMap.get("shipQty"));
			paramMap.put("trstWt", detailMap.get("shipWt"));
			paramMap.put("trstUpr", detailMap.get("shipUpr"));
			paramMap.put("trstAmt", detailMap.get("shipAmt"));
			paramMap.put("realTrstQty", detailMap.get("realShipQty"));
			paramMap.put("realTrstWt", detailMap.get("realShipWt"));
			paramMap.put("realTrstUpr", detailMap.get("realShipUpr"));
			paramMap.put("realTrstAmt", detailMap.get("realShipAmt"));
			paramMap.put("bilgQty", detailMap.get("realShipQty"));
			paramMap.put("bilgWt", detailMap.get("realShipWt"));
			paramMap.put("bilgUpr", detailMap.get("realShipUpr"));
			paramMap.put("bilgAmt", detailMap.get("realShipAmt"));
			ar02Mapper.insertPchsSell(paramMap);
			// 재고정보 update
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			paramMap.put("stockChgCd", "STOCKCHG02");
			if(stockInfo == null) {
				paramMap.put("pchsUpr", detailMap.get("realShipUpr"));
				paramMap.put("sellUpr", detailMap.get("realShipUpr"));
				paramMap.put("stockUpr", detailMap.get("realShipUpr"));
				paramMap.put("stdUpr", detailMap.get("realShipUpr"));
				paramMap.put("stockQty", "-"+detailMap.get("realShipQty"));
			} else {
				paramMap.put("pchsUpr", detailMap.get("pchsUpr"));
				paramMap.put("sellUpr", detailMap.get("sellUpr"));
				paramMap.put("stockUpr", detailMap.get("stockUpr"));
				paramMap.put("stdUpr", detailMap.get("realShipUpr"));
				int stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(detailMap.get("realShipQty"));
				paramMap.put("stockQty", String.valueOf(stockQty));
			}
			sm01Mapper.updateStockSell(paramMap);
		}
		return result;
	}

}