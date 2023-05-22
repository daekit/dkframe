package com.dksys.biz.user.ar.ar07.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR07Mapper {

	int selectMtClosCditCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMtClosCditList(Map<String, String> paramMap);

	List<Map<String, String>> selectClosCditList(Map<String, String> paramMap);
	
	List<Map<String, String>> selectClosCditListClnt(Map<String, String> paramMap);

	int selectMtCloseChkCount(Map<String, String> paramMap);

	int selectMtClosCditPreCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMtCloseCditPreList(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsSellList(Map<String, String> paramMap);

	int selectPreMtCloseChkCount(Map<String, String> paramMap);

	int selectPreMtClosCditCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPreMtClosCditList(Map<String, String> paramMap);

	List<Map<String, String>> selectDailyClosCditList(Map<String, String> paramMap);

	List<Map<String, String>> selectAllCditList(Map<String, String> paramMap);

	List<Map<String, String>> selectAllCditListClnt(Map<String, String> paramMap);

	int selectClosCditListClntCount(Map<String, String> paramMap);

	int selectClosCditListCount(Map<String, String> paramMap);

	int selectAllCditListClntCount(Map<String, String> paramMap);

	int selectAllCditListCount(Map<String, String> paramMap);

	


}