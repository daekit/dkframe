package com.dksys.biz.user.sd.sd07.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD07Mapper {

	Map<String, String> selectClose(Map<String, String> paramMap);
	Map<String, String> selectMaxCloseDay(Map<String, String> paramMap);

	int saveClose(Map<String, String> paramMap);

	int deleteStockClose(Map<String, String> paramMap);
	
	int insertStockClose(Map<String, String> paramMap);

	int deleteCreditClose(Map<String, String> paramMap);

	int insertCreditClose(Map<String, String> paramMap);

	int insertCreditClosePur(Map<String, String> paramMap);

	int chkBilgFlagYn(Map<String, String> paramMap);

	List<Map<String, String>> selectCloseYmList(Map<String, String> paramMap);

}