package com.dksys.biz.user.ar.ar07.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR07Mapper {

	int selectMtClosCditCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMtClosCditList(Map<String, String> paramMap);

	List<Map<String, String>> selectClosCditList(Map<String, String> paramMap);

	int selectMtCloseChkCount(Map<String, String> paramMap);

	int selectMtClosCditPreCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMtCloseCditPreList(Map<String, String> paramMap);

	List<Map<String, String>> selectEtrdpsSellList(Map<String, String> paramMap);

	int selectPreMtCloseChkCount(Map<String, String> paramMap);

	int selectPreMtClosCditCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPreMtClosCditList(Map<String, String> paramMap);

}