package com.dksys.biz.user.pp.pp01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PP01Mapper {

	int selectMesOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap);

	Map<String, String> selectMesOrderDetail(Map<String, String> paramMap);

	List<Map<String, String>> selectOrderDetailList(Map<String, String> paramMap);
	
	int insertOrder(Map<String, String> paramMap);
	
	int insertMesOrder(Map<String, String> paramMap);
	
	int insertOrderDetail(Map<String, String> paramMap);
	
	int insertMesOrderDetail(Map<String, String> paramMap);
	
	int callPL_SD04_MES();

}