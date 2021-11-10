package com.dksys.biz.douzone.dz.dz01.mssqlmapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DZ01Mapper {
	List<Map<String, String>> testSelect(Map<String, String> paramMap);
	
	void testInsert(Map<String, String> paramMap);
}
