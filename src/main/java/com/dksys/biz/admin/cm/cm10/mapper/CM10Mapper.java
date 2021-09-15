package com.dksys.biz.admin.cm.cm10.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM10Mapper {
	
	int selectShipNCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipNList(Map<String, String> paramMap);
	
	int selectOrdrgNCount(Map<String, String> paramMap);

	List<Map<String, String>> selectOrdrgNList(Map<String, String> paramMap);

	int selectReqNCount(Map<String, String> paramMap);

	List<Map<String, String>> selectReqNList(Map<String, String> paramMap);
	
	int selectTaxNCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectTaxNList(Map<String, String> paramMap);

}