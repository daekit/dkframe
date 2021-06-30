package com.dksys.biz.admin.bm.bm01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BM01Mapper {
	
	int selectMaterialCount(Map<String, String> param);
	
	List<Map<String, String>> selectMaterialList(Map<String, String> param);
	
	Map<String, String> selectMaterialInfo(Map<String, String> param);
	
	String selectProductGroup(String prdtCd);
	
	String selectProductGroupNm(String prdtGrp);
	
	int checkOverLap(Map<String, String> param);

	int insertMaterial(Map<String, String> param);
	
	int updateMaterial(Map<String, String> param);

	int deleteMaterial(Map<String, String> param);

}
