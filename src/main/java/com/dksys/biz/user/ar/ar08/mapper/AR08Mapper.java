package com.dksys.biz.user.ar.ar08.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR08Mapper {
	
	int selectCreditCount(Map<String, String> paramMap);

	List<Map<String, String>> selectCreditList(Map<String, String> paramMap);

	long selectBasisAmt(Map<String, String> paramMap);

	
}
