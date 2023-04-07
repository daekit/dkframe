package com.dksys.biz.user.ar.ar16.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR16Mapper {

	int selectLoanAmtCount(Map<String, String> paramMap);

	List<Map<String, String>> selectLoanAmtList(Map<String, String> paramMap);
	
}
