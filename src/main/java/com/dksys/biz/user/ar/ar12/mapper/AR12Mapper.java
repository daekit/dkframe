package com.dksys.biz.user.ar.ar12.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR12Mapper {
	int SelectPtnCreditCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> SelectPtnCreditList(Map<String, String> paramMap);
}
