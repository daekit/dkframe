package com.dksys.biz.admin.bm.bm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.admin.bm.bm02.service.BM02Svc;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class BM02SvcImpl implements BM02Svc {
	
    @Autowired
    BM02Mapper bm02Mapper;
    
    @Autowired
    CM08Svc cm08Svc;

	@Override
	public int selectClntCount(Map<String, String> paramMap) {
		return bm02Mapper.selectClntCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectClntList(Map<String, String> paramMap) {
		return bm02Mapper.selectClntList(paramMap);
	}

	@Override
	public Map<String, String> selectClntInfo(Map<String, String> paramMap) {
		return bm02Mapper.selectClntInfo(paramMap);
	}
	
	@Override
	public void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 거래처 insert 
		bm02Mapper.insertClnt(paramMap);
		
		// 사업부 insert
		List<Map<String, String>> bizdeptList = gson.fromJson(paramMap.get("bizdeptArr"), mapList);
		if(bizdeptList != null) {
			for(Map<String, String> bizdeptMap : bizdeptList) {
				bizdeptMap.put("clntCd", paramMap.get("clntCd"));
				bizdeptMap.put("userId", paramMap.get("userId"));
				bizdeptMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertBizdept(bizdeptMap);
			}
		}
		
		// 담보내역 insert
		List<Map<String, String>> pldgList = gson.fromJson(paramMap.get("pldgArr"), mapList);
		if(pldgList != null) {
			for(Map<String, String> pldgMap : pldgList) {
				pldgMap.put("clntCd", paramMap.get("clntCd"));
				pldgMap.put("userId", paramMap.get("userId"));
				pldgMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertPldg(pldgMap);
			}
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd"), mRequest);
	}
	
	@Override
	public void updateClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new Gson();
		Type stringList = new TypeToken<ArrayList<String>>() {}.getType();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 거래처 update
		bm02Mapper.updateClnt(paramMap);
		
		// 사업부 delete
		bm02Mapper.deleteBizdept(paramMap);
		// 사업부 insert
		List<Map<String, String>> bizdeptList = gson.fromJson(paramMap.get("bizdeptArr"), mapList);
		if(bizdeptList != null) {
			for(Map<String, String> bizdeptMap : bizdeptList) {
				bizdeptMap.put("clntCd", paramMap.get("clntCd"));
				bizdeptMap.put("userId", paramMap.get("userId"));
				bizdeptMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertBizdept(bizdeptMap);
			}
		}
		
		// 담보내역 delete
		bm02Mapper.deletePldg(paramMap);
		// 담보내역 insert
		List<Map<String, String>> pldgList = gson.fromJson(paramMap.get("pldgArr"), mapList);
		if(pldgList != null) {
			for(Map<String, String> pldgMap : pldgList) {
				pldgMap.put("clntCd", paramMap.get("clntCd"));
				pldgMap.put("userId", paramMap.get("userId"));
				pldgMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertPldg(pldgMap);
			}
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd"), mRequest);
		// 파일 삭제
		List<String> deleteFileList = gson.fromJson(paramMap.get("deleteFileArr"), stringList);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
	}

	@Override
	public void unuseClnt(Map<String, String> paramMap) {
		bm02Mapper.unuseClnt(paramMap);
		bm02Mapper.unusePldg(paramMap);
		bm02Mapper.unuseBizdept(paramMap);
	}

	@Override
	public Map<String, String> selectMngInfo(Map<String, String> paramMap) {
		return bm02Mapper.selectMngInfo(paramMap);
	}
}