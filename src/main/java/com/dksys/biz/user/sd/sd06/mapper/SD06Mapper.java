package com.dksys.biz.user.sd.sd06.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD06Mapper {
	
	
	int selectUprCount(Map<String, String> param);
	
	List<Map<String, String>> selectUprList(Map<String, String> param);

//	int insertUpr(Map<String, String> param);

//	int deleteUpr(Map<String, String> param);

//	int updateUpr(Map<String, String> param);
	
	Map<String, String> selectUprInfo(Map<String, String> param);

	int selectOneMasterCount(Map<String, String> param);
	
	int selectDetail01Count(Map<String, String> param);
	
	List<Map<String, String>> selectDetail01List(Map<String, String> param);

	int selectDetail02Count(Map<String, String> param);
	
	List<Map<String, String>> selectDetail02List(Map<String, String> param);

	Map<String, String> seletOneMaster(Map<String, String> param);

	Map<String, String> seletOneDetail01(Map<String, String> param);
	
	Map<String, String> seletOneDetail02(Map<String, String> param);

	int insertOneMaster(Map<String, String> param);
	
	int insertOneDetail01(Map<String, String> param);
	
	int insertOneDetail02(Map<String, String> param);
	
	int updateOneDetail01(Map<String, String> param);
	
	int updateOneDetail02(Map<String, String> param);

	int selectUprClntCount(Map<String, String> param);

	List<Map<String, String>> selectUprClntList(Map<String, String> param);

	int selectOneMasterClntCount(Map<String, String> param);

	Map<String, String> seletOneMasterClnt(Map<String, String> param);

	int insertOneMasterClnt(Map<String, String> param);

	int updateUseYnClnt(Map<String, String> param);



}
