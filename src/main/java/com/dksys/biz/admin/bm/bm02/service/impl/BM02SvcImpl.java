package com.dksys.biz.admin.bm.bm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.admin.bm.bm02.service.BM02Svc;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
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
	public Map<String, Object> selectClntInfo(Map<String, String> paramMap) {
		return bm02Mapper.selectClntInfo(paramMap);
	}
	
	@Override
	public void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 거래처 insert 
		bm02Mapper.insertClnt(paramMap);
		
		List<Map<String, String>> bizdeptList = gson.fromJson(paramMap.get("bizdeptArr"), mapList);
		if(bizdeptList != null) {
			// 사업부 insert
			for(Map<String, String> bizdeptMap : bizdeptList) {
				bizdeptMap.put("clntCd_P", paramMap.get("clntCd_P"));
				bizdeptMap.put("userId", paramMap.get("userId"));
				bizdeptMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertBizdept(bizdeptMap);
			}
		}
		
		List<Map<String, String>> pldgList = gson.fromJson(paramMap.get("pldgArr"), mapList);
		if(pldgList != null) {
			// 담보내역 insert
			for(Map<String, String> pldgMap : pldgList) {
				pldgMap.put("clntCd_P", paramMap.get("clntCd_P"));
				pldgMap.put("userId", paramMap.get("userId"));
				pldgMap.put("pgmId", paramMap.get("pgmId"));
				bm02Mapper.insertPldg(pldgMap);
			}
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd_P"), mRequest);
	}
	
	@Override
	public void updateClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new Gson();
		Type stringList = new TypeToken<ArrayList<String>>() {}.getType();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 거래처 update
		bm02Mapper.updateClnt(paramMap);
		
		
		List<Map<String, String>> bizdeptList = gson.fromJson(paramMap.get("bizdeptArr"), mapList);
		if(bizdeptList != null) {
			// 사업부 delete
		//	bm02Mapper.deleteBizdept(paramMap);
			// 사업부 insert
			for(Map<String, String> bizdeptMap : bizdeptList) {
				bizdeptMap.put("clntCd_P", paramMap.get("clntCd_P"));
				bizdeptMap.put("userId", paramMap.get("userId"));
				bizdeptMap.put("pgmId", paramMap.get("pgmId"));
				bizdeptMap.put("udtId",  paramMap.get("userId"));
				bizdeptMap.put("udtPgm", paramMap.get("pgmId"));
				
				if(bizdeptMap.get("bizdeptSn")==null || "".equals(bizdeptMap.get("bizdeptSn"))) {
					bm02Mapper.insertBizdept(bizdeptMap);
				}else {
					bm02Mapper.updateBizdept(bizdeptMap);
				}
			}
		}
		
		List<Map<String, String>> pldgList = gson.fromJson(paramMap.get("pldgArr"), mapList);
		if(pldgList != null) {
			// 담보내역 insert
			for(Map<String, String> pldgMap : pldgList) {
				pldgMap.put("clntCd_P", paramMap.get("clntCd_P"));
				pldgMap.put("userId", paramMap.get("userId"));
				pldgMap.put("pgmId",  paramMap.get("pgmId"));
				pldgMap.put("udtId",  paramMap.get("userId"));
				pldgMap.put("udtPgm", paramMap.get("pgmId"));
		
				if(pldgMap.get("pldgSn")==null || "".equals(pldgMap.get("pldgSn"))) {
				    bm02Mapper.insertPldg(pldgMap);
				}else {
					bm02Mapper.updatePldg(pldgMap);
				}
				
			}
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd_P"), mRequest);
		// 파일 삭제
		List<String> deleteFileList = gson.fromJson(paramMap.get("deleteFileArr"), stringList);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
	}

	@Override
	public void deleteClntPldg(Map<String, Object> paramMap) {
		List<Map<String, String>> pldgList = (List<Map<String, String>>) paramMap.get("pldgArr");
		if(pldgList != null) {
			// 담보내역 delete
			for(Map<String, String> pldgMap : pldgList) {
				bm02Mapper.deletePldg(pldgMap);				
			}
		}
	}

	@Override
	public void deleteClntBizdept(Map<String,  Object> paramMap) {

		List<Map<String, String>> bizdeptList =(List<Map<String, String>>) paramMap.get("bizdeptArr");
		if(bizdeptList != null) {
			// 담보내역 update
			for(Map<String, String> bizdeptMap : bizdeptList) {
				bm02Mapper.deleteBizdept(bizdeptMap);				
			}
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

	@Override
	public Map<String, String> selectCrnDupChk(Map<String, String> paramMap) {
		return bm02Mapper.selectCrnDupChk(paramMap);
	}
}