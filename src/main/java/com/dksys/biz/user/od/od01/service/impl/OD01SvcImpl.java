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
		return od01Mapper.deleteOrder(paramMap);
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
		int result = od01Mapper.updateConfirm(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		String ordrgSeq = paramMap.get("ordrgSeq");
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", ordrgSeq);
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od01Mapper.updateConfirmDetail(detailMap);
			detailMap = od01Mapper.selectOrderDetailInfo(detailMap);
			paramMap.putAll(detailMap);
			paramMap.put("selpchCd", "SELPCH1");
			ar02Mapper.insertPchsSell(paramMap);
			if("Y".equals(paramMap.get("dirtrsYn"))) {
				paramMap.put("selpchCd", "SELPCH2");
				ar02Mapper.insertPchsSell(paramMap);
			}
		}
		return result;
	}

}