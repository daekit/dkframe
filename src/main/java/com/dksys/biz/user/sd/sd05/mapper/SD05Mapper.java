package com.dksys.biz.user.sd.sd05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD05Mapper {
	
	
	List<Map<String, String>> selectProjectList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectcoCdList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectNameList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectClntList(Map<String, String> param);
	
	Map<String, String> selectProjectKeyList(Map<String, String> param);
	
	int selectProjectCount(Map<String, String> param);
	
	Map<String, String> selectPrjInfo(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdDetail(Map<String, String> paramMap);

	List<Map<String, String>> selectShipmentDetail(Map<String, String> paramMap);

	List<Map<String, String>> selectProjectDtl(Map<String, String> paramMap);
	
	int insertProject(Map<String, String> paramMap);
	
	int insertProjectDtl(Map<String, String> paramMap);

	int updateProject(Map<String, String> param);

	int updatePrjctDtl(Map<String, String> param);
	
	int selectConfirmCount(Map<String, String> paramMap);
	
	int deleteProject(Map<String, String> param);
	
	int deleteProjectDtl(Map<String, String> param);
	
	List<Map<String, String>> selectPrdtDivCd(Map<String, String> param);
	
	List<Map<String, String>> prdtDivCombo(Map<String, String> param);
	
	List<Map<String, String>> prdtSpecCombo(Map<String, String> param);

	Map<String, String> selectMakerPchsClntCd(Map<String, String> paramMap);
	
	int selectPrjtMngTernKeyCount(Map<String, String> param);
	
	List<Map<String, String>> selectPrjtMngTernKey(Map<String, String> param);
	
	int selectChkOrdrgYn(Map<String, String> param);
	
}
