package com.dksys.biz.user.sd.sd02.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD02Svc {
	
	public int selectSellCount(Map<String, String> param);
	
	public List<Map<String, String>> selectSellList(Map<String, String> param);
	
	Map<String, String> selectPlanInfo(Map<String, String> paramMap);
	
	public int insertPlan(Map<String, String> param);

	public int updatePlan(Map<String, String> param);
	
	public int copyInsert(Map<String, String> param);

	public int deleteCopy(Map<String, String> param);
	
	int deletePlan(Map<String, Object> paramMap);
	
	public List<Map<String, String>> selectSellDailyRep(Map<String, String> param);
	
	public List<Map<String, String>> selectSellListInd(Map<String, String> param);

}