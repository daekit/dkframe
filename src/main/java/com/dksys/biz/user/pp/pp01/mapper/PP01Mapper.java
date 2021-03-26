package com.dksys.biz.user.pp.pp01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PP01Mapper {

	int selectMesOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap);

}