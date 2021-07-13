package com.dksys.biz.user.sd.sd02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD02Mapper {
	
	int selectSellCount(Map<String, String> param);
	
	List<Map<String, String>> selectSellList(Map<String, String> param);
	
	Map<String, String> selectPlanInfo(Map<String, String> paramMap);
	
	int insertPlan(Map<String, String> param);
	
	int updatePlan(Map<String, String> param);
	
	int copyInsert(Map<String, String> param);
	
	int deleteCopy(Map<String, String> param);
	
	int deletePlan(String paramMap);
	
	List<Map<String, String>> selectSellDailyRep(Map<String, String> param);
	
	List<Map<String, String>> selectSellListInd(Map<String, String> param);
}
