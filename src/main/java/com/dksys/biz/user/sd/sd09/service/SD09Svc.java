package com.dksys.biz.user.sd.sd09.service;

import java.util.List;
import java.util.Map;

public interface SD09Svc {

	public List<Map<String, String>> selectSiteList(Map<String, String> param);
	
	public int selectSiteCount(Map<String, String> param);
	
	public Map<String, Object> selectSiteDetail(Map<String, String> param);
	
	public String insertSite(Map<String, String> param);

	public int updateSite(Map<String, String> param);
	
	public int deleteSite(Map<String, Object> param);
	
	public int updateSiteYn(Map<String, Object> param); // 현장 종료여부

	public List<Map<String, String>> selectSitePrdtList(Map<String, String> param);

	public int insertSitePrdt(Map<String, String> param);

	public int updateSitePrdt(Map<String, String> param);

	void deleteSitePrdt(List<Map<String, String>> paramList);

	public int insertSiteTrans(Map<String, String> param);

	public int updateSiteTrans(Map<String, String> param);

	void deleteSiteTrans(List<Map<String, String>> paramList);
	
	public Map<String, String> selectClntFromWh(Map<String, String> param);

	Map<String, Object> insertedSite(Map<String, String> paramMap);
	
}