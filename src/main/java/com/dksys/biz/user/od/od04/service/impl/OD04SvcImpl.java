package com.dksys.biz.user.od.od04.service.impl;

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
import com.dksys.biz.user.od.od04.mapper.OD04Mapper;
import com.dksys.biz.user.od.od04.service.OD04Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class OD04SvcImpl implements OD04Svc {
	
	@Autowired
	OD04Mapper od04Mapper;

    @Autowired
    CM08Svc cm08Svc;
	
	@Override
	public int selectReqListCount(Map<String, String> param) {
		return od04Mapper.selectReqListCount(param);
	}
	
	@Override
	public List<Map<String, String>> selectReqList(Map<String, String> param) {
		return od04Mapper.selectReqList(param);
	}
	
	@Override
	public Map<String, Object> selectReq(Map<String, String> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(param.get("reqDt")+param.get("reqSeq")));
		returnMap.put("reqInfo", od04Mapper.selectReq(param));
		returnMap.put("reqDetail", od04Mapper.selectReqDetail(param));
		System.out.println("111");
		System.out.println(returnMap);
		return returnMap;
	} 
	
	@Override
	public Map<String, Object> selectReqOrder(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("reqInfo", od04Mapper.selectReqOrder(param));
		returnMap.put("reqDetail", od04Mapper.selectReqOrderDetail(param));
		return returnMap;
	} 
	
	@Override
	public List<Map<String, String>> selectReqDetail(Map<String, String> param) {
		return od04Mapper.selectReqDetail(param);
	} 
    
	@Override
	public int insertReq(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = od04Mapper.insertReq(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 출하상세 delete
		od04Mapper.deleteReqDetail(paramMap);
		// 출하상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("reqSeq", paramMap.get("reqSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od04Mapper.insertReqDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_OD04M01", paramMap.get("reqDt")+paramMap.get("reqSeq"), mRequest);
		return result;
	}
    
	@Override
	public int deleteReq(Map<String, String> paramMap) {
		List<Map<String, String>> fileList = cm08Svc.selectFileList(paramMap.get("reqDt")+paramMap.get("reqSeq"));
		for(Map<String, String> file : fileList) {
			cm08Svc.deleteFile(file.get("fileKey"));
		}
		int result = od04Mapper.deleteReq(paramMap);
		result = od04Mapper.deleteReqDetail(paramMap);
		return result;
	}
    
	@Override
	public int updateReq(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = od04Mapper.updateReq(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 출하상세 delete
		od04Mapper.deleteReqDetail(paramMap);
		// 출하상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("reqSeq", paramMap.get("reqSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			od04Mapper.insertReqDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_OD04M01", paramMap.get("reqDt")+paramMap.get("reqSeq"), mRequest);
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		return result;
	}
	
	@Override
	public int updateRecpt(Map<String, String> paramMap) {
		int result = 0;
		result = od04Mapper.updateRecpt(paramMap.get("reqSeq"));
		return result;
	}
    
	@Override
	public int updateRecptList(Map<String, Object> paramMap) {
		int result = 0;
		List<String> reqSeqArr = (List<String>) paramMap.get("reqSeqArr");
		for(String reqSeq : reqSeqArr) {
			result = od04Mapper.updateRecpt(reqSeq);
		}
		return result;
	}
	
}