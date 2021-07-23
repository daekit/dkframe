package com.dksys.biz.user.sd.sd05.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD05Svc {

	public List<Map<String, String>> selectProjectList(Map<String, String> param);
	
	public List<Map<String, String>> selectProjectcoCdList(Map<String, String> param);

	public List<Map<String, String>> selectProjectNameList(Map<String, String> param);
	
	public List<Map<String, String>> selectProjectClntList(Map<String, String> param);
	
	public Map<String, String> selectProjectKeyList(Map<String, String> param);
	
	public int selectProjectCount(Map<String, String> param);
	
	Map<String, Object> selectPrjInfo(Map<String, String> paramMap);
	
	int insertProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int updateProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	int selectConfirmCount(Map<String, String> paramMap);
	
	int deleteProject(Map<String, String> param);
	
	void deleteProjectDtl(List<Map<String, String>> paramList);

	public List<Map<String, String>> selectPrdtDivCd(Map<String, String> param);

	public List<Map<String, String>> prdtDivCombo(Map<String, String> param);

	public List<Map<String, String>> prdtSpecCombo(Map<String, String> param);

	public  Map<String, String> selectMakerPchsClntCd(Map<String, String> paramMap);

	public  int selectChkOrdrgYn(Map<String, String> paramMap);

}