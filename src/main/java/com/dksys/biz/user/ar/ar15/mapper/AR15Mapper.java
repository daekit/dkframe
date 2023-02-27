package com.dksys.biz.user.ar.ar15.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR15Mapper {
	
	int selectSalesMngYyListCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectSalesMngYyList(Map<String, String> paramMap);
	
}
