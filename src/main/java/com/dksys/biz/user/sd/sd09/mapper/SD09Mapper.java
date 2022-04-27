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

	int updateSiteYn(Map<String, String> paramMap);
	
 // Site 상세 사항.
	List<Map<String, String>> selectSitePrdtList(Map<String, String> paramMap);

	int insertSitePrdt(Map<String, String> paramMap);
	
	int updateSitePrdt(Map<String, String> paramMap);
	
	int deleteSitePrdt(Map<String, String> paramMap);
	
 // Site 운반비 상세 사항.
	List<Map<String, String>> selectSiteTransList(Map<String, String> paramMap);
	
	int insertSiteTrans(Map<String, String> paramMap);
	
	int updateSiteTrans(Map<String, String> paramMap);
	
	int deleteSiteTrans(Map<String, String> paramMap);
	
	int deleteSiteDtl(Map<String, String> paramMap);
	
	int deleteSiteTDtl(Map<String, String> paramMap);
	
	String selectSiteCdFind(Map<String, String> paramMap);

	Map<String, String> selectClntFromWh(Map<String, String> paramMap);

	Map<String, String> selectMinSite(Map<String, String> siteMap);

	Map<String, Object> insertedSite(Map<String, String> paramMap);

}