package com.dksys.biz.user.sd.sd05.service.impl;

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
import com.dksys.biz.user.sd.sd05.mapper.SD05Mapper;
import com.dksys.biz.user.sd.sd05.service.SD05Svc;
import com.dksys.biz.user.sd.sd09.mapper.SD09Mapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class SD05SvcImpl implements SD05Svc {
	
    @Autowired
    SD05Mapper sd05Mapper;
	
    @Autowired
    SD09Mapper sd09Mapper;

    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public List<Map<String, String>> selectProjectList(Map<String, String> param) {
		return sd05Mapper.selectProjectList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectcoCdList(Map<String, String> param) {
		return sd05Mapper.selectProjectcoCdList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectNameList(Map<String, String> param) {
		return sd05Mapper.selectProjectNameList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectClntList(Map<String, String> param) {
		return sd05Mapper.selectProjectClntList(param);
	}
	
	@Override
	public Map<String, String> selectProjectKeyList(Map<String, String> param) {
		return sd05Mapper.selectProjectKeyList(param);
	}
	
	@Override
	public int selectProjectCount(Map<String, String> param) {
		return sd05Mapper.selectProjectCount(param);
	}
	
	@Override
	public Map<String, Object> selectPrjInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("prjctCd")));
		returnMap.put("prjInfo", sd05Mapper.selectPrjInfo(paramMap));
		returnMap.put("ordDetail", sd05Mapper.selectOrdDetail(paramMap));
		returnMap.put("shipmentDetail", sd05Mapper.selectShipmentDetail(paramMap));
		returnMap.put("prjctDtl", sd05Mapper.selectProjectDtl(paramMap));
		return returnMap;
	}
	
	@Override
	public int insertProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd05Mapper.insertProject(paramMap);
		paramMap.put("siteNm", paramMap.get("prjctNm"));
		paramMap.put("siteAddrZip", paramMap.get("prjctAddrZip"));
		paramMap.put("siteAddr", paramMap.get("prjctAddr"));
		paramMap.put("siteAddrSub", paramMap.get("prjctAddrSub"));
		paramMap.put("siteMngNm", paramMap.get("prjctMngNm"));
		sd09Mapper.insertSite(paramMap);
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gson.fromJson(paramMap.get("detailArr"), dtlMap);
		
		for(Map<String, String> dtl : dtlParam) {
			dtl.put("coCd", paramMap.get("coCd"));
			dtl.put("prjctCd", paramMap.get("prjctCd"));
			dtl.put("userId", paramMap.get("userId"));
			dtl.put("pgmId", paramMap.get("pgmId"));
			sd05Mapper.insertProjectDtl(dtl);
		}
		//sd05Mapper.deleteProject(paramMap);
		cm08Svc.uploadFile("TB_SD05M01", paramMap.get("prjctCd"), mRequest);
		return result;
	}

	@Override
	public int updateProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd05Mapper.updateProject(paramMap);
		cm08Svc.uploadFile("TB_SD05M01", paramMap.get("prjctCd"), mRequest);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		Gson gsonDtl = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gsonDtl.fromJson(paramMap.get("detailArr"), dtlMap);
		sd05Mapper.deleteProjectDtl(paramMap);
		for(Map<String, String> dtl : dtlParam) {
			dtl.put("coCd", paramMap.get("coCd"));
			dtl.put("prjctCd", paramMap.get("prjctCd"));
			dtl.put("userId", paramMap.get("userId"));
			dtl.put("pgmId", paramMap.get("pgmId"));
			sd05Mapper.insertProjectDtl(dtl);
		}
		return result;
	}
	
	@Override
	public int deleteProject(Map<String, String> param) {
		//sd05Mapper.deleteProjectDtl(param); 마스터테이블 사용여부만 n으로
		return sd05Mapper.deleteProject(param);
	}
	
	@Override
	public int selectConfirmCount(Map<String, String> paramMap) {
		return sd05Mapper.selectConfirmCount(paramMap);
	}
    
	@Override
	public List<Map<String, String>> selectPrdtDivCd(Map<String, String> param) {
		return sd05Mapper.selectPrdtDivCd(param);
	}
    
	@Override
	public List<Map<String, String>> prdtDivCombo(Map<String, String> param) {
		return sd05Mapper.prdtDivCombo(param);
	}
    
	@Override
	public List<Map<String, String>> prdtSizeCombo(Map<String, String> param) {
		return sd05Mapper.prdtSizeCombo(param);
	}
}