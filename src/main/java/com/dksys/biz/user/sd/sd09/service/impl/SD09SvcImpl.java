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
@Transactional(rollbackFor = Exception.class)
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
		returnMap.put("siteTrans", 		 sd09Mapper.selectSiteTransList(paramMap));
		
		return returnMap;
		
	}

	@Override
	public String insertSite(Map<String, String> paramMap) {
		String siteCd = sd09Mapper.selectSiteCd(paramMap); 
		paramMap.put("siteCd", siteCd);
		sd09Mapper.insertSite(paramMap);
		
		//  이하의 2가지는 공장에서 현장 등록 및 수정시 사용함.  SD0902P01
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gson.fromJson(paramMap.get("detailArr"), dtlMap);
        if(dtlParam != null) {        	
			for(Map<String, String> dtl : dtlParam) {
				dtl.put("coCd",    paramMap.get("coCd"));
				dtl.put("prjctCd", paramMap.get("prjctCd"));
				dtl.put("siteCd",  siteCd);
				dtl.put("userId",  paramMap.get("userId"));
				dtl.put("pgmId",   paramMap.get("pgmId"));
				sd09Mapper.insertSitePrdt(dtl);		
			}
        }
		gson = new GsonBuilder().disableHtmlEscaping().create();
		Type transMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> transParam = gson.fromJson(paramMap.get("transArr"), transMap);
        if(transParam != null) {        	 
			for(Map<String, String> trans : transParam) {
				trans.put("coCd",    paramMap.get("coCd"));
				trans.put("prjctCd", paramMap.get("prjctCd"));
				trans.put("siteCd",  siteCd);
				trans.put("userId",  paramMap.get("userId"));
				trans.put("pgmId",   paramMap.get("pgmId"));
				sd09Mapper.insertSiteTrans(trans);		
			}
        }
		return siteCd;
	}

	@Override
	public int updateSite(Map<String, String> paramMap) {
		sd09Mapper.deleteSiteDtl(paramMap);
		sd09Mapper.deleteSiteTDtl(paramMap);
		
		//  이하의 2가지는 공장에서 현장 등록 및 수정시 사용함.  SD0902P01
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gson.fromJson(paramMap.get("detailArr"), dtlMap);
        if(dtlParam != null) {        	
			for(Map<String, String> dtl : dtlParam) {
				dtl.put("coCd",    paramMap.get("coCd"));
				dtl.put("prjctCd", paramMap.get("prjctCd"));
				dtl.put("siteCd",  paramMap.get("siteCd"));
				dtl.put("userId",  paramMap.get("userId"));
				dtl.put("pgmId",   paramMap.get("pgmId"));
				sd09Mapper.insertSitePrdt(dtl);		
			}
        }
		gson = new GsonBuilder().disableHtmlEscaping().create();
		Type transMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> transParam = gson.fromJson(paramMap.get("transArr"), transMap);
        if(transParam != null) {        	 
			for(Map<String, String> trans : transParam) {
				trans.put("coCd",    paramMap.get("coCd"));
				trans.put("prjctCd", paramMap.get("prjctCd"));
				trans.put("siteCd",  paramMap.get("siteCd"));
				trans.put("userId",  paramMap.get("userId"));
				trans.put("pgmId",   paramMap.get("pgmId"));
				sd09Mapper.insertSiteTrans(trans);		
			}
        }
		return sd09Mapper.updateSite(paramMap);
	}

	@Override
	@SuppressWarnings("all")
	public int deleteSite(Map<String, Object> paramMap) {		
		System.out.println("paramMap : " + paramMap.toString());
		int result = 0;
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += sd09Mapper.deleteSite(detailMap);
		}
		 return result; 
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
		if(detailList != null) {
			for(Map<String, String> detailMap : detailList) {

				detailMap.put("coCd",    paramMap.get("coCd"));
				detailMap.put("siteCd",  paramMap.get("siteCd"));
				detailMap.put("prjctCd", paramMap.get("prjctCd"));
				detailMap.put("userId",  paramMap.get("userId"));
				detailMap.put("pgmId",   paramMap.get("pgmId"));
				result += sd09Mapper.insertSitePrdt(detailMap);
			}
		}
		return result;
	}

	@Override
	public int insertSiteTrans(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("transArr"), dtlMap);
		if(detailList != null) {
			for(Map<String, String> detailMap : detailList) {

				detailMap.put("coCd",    paramMap.get("coCd"));
				detailMap.put("siteCd",  paramMap.get("siteCd"));
				detailMap.put("prjctCd", paramMap.get("prjctCd"));
				detailMap.put("userId",  paramMap.get("userId"));
				detailMap.put("pgmId",   paramMap.get("pgmId"));
				result += sd09Mapper.insertSiteTrans(detailMap);
			}
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
	public int updateSiteTrans(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("transArr"), dtlMap);
		for(Map<String, String> detailMap : detailList) {

			detailMap.put("coCd",    paramMap.get("coCd"));
			detailMap.put("siteCd",  paramMap.get("siteCd"));
			detailMap.put("prjctCd", paramMap.get("prjctCd"));
			detailMap.put("userId",  paramMap.get("userId"));
			detailMap.put("pgmId",   paramMap.get("pgmId"));
			String siteTransSeq = detailMap.get("siteTransSeq").toString();
			if("".equals(siteTransSeq) || "0".equals(siteTransSeq)) {
				result += sd09Mapper.insertSiteTrans(detailMap);
			}else {
				result += sd09Mapper.updateSiteTrans(detailMap);	
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

	@Override
	public void deleteSiteTrans(List<Map<String, String>> paramList) {
		for(Map<String, String> param : paramList) {
			sd09Mapper.deleteSiteTrans(param);
		}	
	}

	@Override
	public Map<String, String> selectClntFromWh(Map<String, String> paramMap) {		
		return sd09Mapper.selectClntFromWh(paramMap);
		
	}
	

	@Override
	public Map<String, Object> insertedSite(Map<String, String> paramMap) {
		Map<String, Object> insertedSite = sd09Mapper.insertedSite(paramMap);

		return insertedSite;
	}
}