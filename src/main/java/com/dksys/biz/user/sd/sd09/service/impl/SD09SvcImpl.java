package com.dksys.biz.user.sd.sd09.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd09.mapper.SD09Mapper;
import com.dksys.biz.user.sd.sd09.service.SD09Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class SD09SvcImpl implements SD09Svc {
	
    @Autowired
    SD09Mapper sd09Mapper;
    
	@Override
	public int selectSiteCount(Map<String, String> paramMap) {
		return sd09Mapper.selectSiteCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSiteList(Map<String, String> paramMap) {
		return sd09Mapper.selectSiteList(paramMap);
	}

	@Override
	public Map<String, Object> selectSiteDetail(Map<String, String> paramMap) {
//		return sd09Mapper.selectSiteDetail(paramMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("siteInfo",        sd09Mapper.selectSiteDetail(paramMap));
		returnMap.put("siteDtl",         sd09Mapper.selectSitePrdtList(paramMap));
		
		return returnMap;
		
	}

	@Override
	public String insertSite(Map<String, String> paramMap) {
		String siteCd = sd09Mapper.selectSiteCd(paramMap); 
		paramMap.put("siteCd", siteCd);
		sd09Mapper.insertSite(paramMap);
		insertSitePrdt(paramMap);
		return siteCd;
	}

	@Override
	public int updateSite(Map<String, String> paramMap) {
		updateSitePrdt(paramMap);
		return sd09Mapper.updateSite(paramMap);
	}

	@Override
	public int deleteSite(Map<String, String> paramMap) {
//	     deleteSitePrdt(paramMap);
		sd09Mapper.deleteSitePrdt(paramMap);
		 return sd09Mapper.deleteSite(paramMap); 
	}

	@Override
	@SuppressWarnings("all")
	public int updateSiteYn(Map<String, Object> paramMap) {		
		int result = 0;
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += sd09Mapper.updateSiteYn(detailMap);
		}
		return result;
	}
	
  // 현장 제품상세
	@Override
	public List<Map<String, String>> selectSitePrdtList(Map<String, String> paramMap) {
		return sd09Mapper.selectSitePrdtList(paramMap);
	}

	@Override
	public int insertSitePrdt(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), dtlMap);
		for(Map<String, String> detailMap : detailList) {

			detailMap.put("coCd",    paramMap.get("coCd"));
			detailMap.put("siteCd",  paramMap.get("siteCd"));
			detailMap.put("prjctCd", paramMap.get("prjctCd"));
			detailMap.put("userId",  paramMap.get("userId"));
			detailMap.put("pgmId",   paramMap.get("pgmId"));
			result += sd09Mapper.insertSitePrdt(detailMap);
		}
		return result;
	}

	@Override
	public int updateSitePrdt(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), dtlMap);
		for(Map<String, String> detailMap : detailList) {

			detailMap.put("coCd",    paramMap.get("coCd"));
			detailMap.put("siteCd",  paramMap.get("siteCd"));
			detailMap.put("prjctCd", paramMap.get("prjctCd"));
			detailMap.put("userId",  paramMap.get("userId"));
			detailMap.put("pgmId",   paramMap.get("pgmId"));
			String siteSeq = detailMap.get("siteSeq").toString();
			if("".equals(siteSeq) || "0".equals(siteSeq)) {
				result += sd09Mapper.insertSitePrdt(detailMap);
			}else {
				result += sd09Mapper.updateSitePrdt(detailMap);	
			}
		}
		return result;
	}

	@Override
	public void deleteSitePrdt(List<Map<String, String>> paramList) {
		for(Map<String, String> param : paramList) {
			sd09Mapper.deleteSitePrdt(param);
		}	
	}

}