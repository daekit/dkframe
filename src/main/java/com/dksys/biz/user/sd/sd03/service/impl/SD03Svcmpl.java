package com.dksys.biz.user.sd.sd03.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.sd.sd03.mapper.SD03Mapper;
import com.dksys.biz.user.sd.sd03.service.SD03Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
public class SD03Svcmpl implements SD03Svc {
	
    @Autowired
    SD03Mapper sd03Mapper;
    
    @Autowired
    CM08Svc cm08Svc; 

    @Override
	public List<Map<String, String>> selectPrdtCodeList(Map<String, String> param) {
		return sd03Mapper.selectPrdtCodeList(param);
	}

    @Override
	public List<Map<String, String>> selectPrdtCd(Map<String, String> param) {
    	return sd03Mapper.selectPrdtCd(param);
	}
    
	@Override
	public int selectUprCount(Map<String, String> param) {
		return sd03Mapper.selectUprCount(param);
	}

	@Override
	public List<Map<String, String>> selectMainEstList(Map<String, String> param) {
		return sd03Mapper.selectMainEstList(param);
	}
	
	@Override
	public Map<String, String> selectEstInfo(Map<String, String> paramMap) {
		return sd03Mapper.selectEstInfo(paramMap);
	}
	
	@Override
	public void insertEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		sd03Mapper.insertEst(paramMap);
		cm08Svc.uploadFile("TB_SD03M01", paramMap.get("estNo"), mRequest);
		// 견적서 delete
		//sd03Mapper.deleteEstDetail(paramMap);
		// 견적서 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("estNo", paramMap.get("estNo"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			sd03Mapper.insertEstDetail(detailMap);
		}
	}

	@Override
	public int updateEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd03Mapper.updateEst(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type stringList = new TypeToken<ArrayList<String>>() {}.getType();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 견적상세 delete
		sd03Mapper.deleteEstDetail(paramMap);
		
		//파일업로드
		cm08Svc.uploadFile("TB_SD03M01", paramMap.get("estNo"), mRequest);
		//파일삭제
		List<String> deleteFileList = gson.fromJson(paramMap.get("deleteFileArr"),stringList);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> estMap : detailList) {
			estMap.put("estNo", paramMap.get("estNo"));
			estMap.put("userId", paramMap.get("userId"));
			estMap.put("pgmId", paramMap.get("pgmId"));
			sd03Mapper.insertEstDetail(estMap);
		}
		return result;
	}
}