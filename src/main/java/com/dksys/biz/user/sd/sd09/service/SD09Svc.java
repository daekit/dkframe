package com.dksys.biz.user.sd.sd09.service;

import java.util.List;
import java.util.Map;

public interface SD09Svc {

	public List<Map<String, String>> selectSiteList(Map<String, String> param);
	
	public int selectSiteCount(Map<String, String> param);
	
	public Map<String, String> selectSiteDetail(Map<String, String> param);
	
	public String insertSite(Map<String, String> param);

	public int updateSite(Map<String, String> param);
	
	public int deleteSite(Map<String, String> param);
	
	public int updateSiteYn(Map<String, Object> param); // 현장 종료여부
	
}