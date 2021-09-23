package com.dksys.biz.user.sd.sd03.service.impl;

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
import com.dksys.biz.user.sd.sd03.mapper.SD03Mapper;
import com.dksys.biz.user.sd.sd03.service.SD03Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD03Svcmpl implements SD03Svc {
	
    @Autowired
    SD03Mapper sd03Mapper;
    
    @Autowired
    CM08Svc cm08Svc; 
    
	@Override
	public int selectEstCount(Map<String, String> param) {
		return sd03Mapper.selectEstCount(param);
	}

	@Override
	public List<Map<String, Object>> selectEstList(Map<String, String> param) {
		return sd03Mapper.selectEstList(param);
	}
	
	@Override
	public Map<String, Object> selectEstInfo(Map<String, String> paramMap) {
		return sd03Mapper.selectEstInfo(paramMap);
	}
	
	@Override
	public void insertEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		paramMap.put("estNo", sd03Mapper.selectEstNo());
		
		// 견적서 insert
		sd03Mapper.insertEst(paramMap);
		
		// 견적상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("estNo", paramMap.get("estNo"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			sd03Mapper.insertEstDetail(detailMap);
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_SD03M01", paramMap.get("estNo"), mRequest);
	}

	@Override
	public int updateEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type stringList = new TypeToken<ArrayList<String>>() {}.getType();
		
		// 견적 update
		int result = sd03Mapper.updateEst(paramMap);
		
		// 견적상세 delete
		sd03Mapper.deleteEstDetail(paramMap);
		// 견적상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> estMap : detailList) {
			estMap.put("estNo", paramMap.get("estNo"));
			estMap.put("userId", paramMap.get("userId"));
			estMap.put("pgmId", paramMap.get("pgmId"));
			sd03Mapper.insertEstDetail(estMap);
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_SD03M01", paramMap.get("estNo"), mRequest);
		// 파일 삭제
		List<String> deleteFileList = gson.fromJson(paramMap.get("deleteFileArr"),stringList);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		
		return result;
	}
	
	@Override
	public int deleteEst(Map<String, String> paramMap) {
		int result = sd03Mapper.deleteEst(paramMap);
		result += sd03Mapper.deleteEstDetail(paramMap);
		return result;
	}
}