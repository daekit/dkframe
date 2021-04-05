package com.dksys.biz.user.sd.sd09.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD09Mapper {

	int selectSiteCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSiteList(Map<String, String> paramMap);

	Map<String, String> selectSiteDetail(Map<String, String> paramMap);
	
	String selectSiteCd(Map<String, String> paramMap);

	void insertSite(Map<String, String> paramMap);
	
	int updateSite(Map<String, String> paramMap);
	
	int deleteSite(Map<String, String> paramMap);

}