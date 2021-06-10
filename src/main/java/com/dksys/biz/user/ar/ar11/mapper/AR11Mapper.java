package com.dksys.biz.user.ar.ar11.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR11Mapper {
	int EtrdpsPayCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> EtrdpsPayList(Map<String, String> paramMap);
}
