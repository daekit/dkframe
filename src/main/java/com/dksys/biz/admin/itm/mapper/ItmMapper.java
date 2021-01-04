package com.dksys.biz.admin.itm.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItmMapper {
	
	List<Map<String, String>> selectItmList();

	int insertItm(Map<String, String> param);

	int deleteItm(Map<String, String> param);

	int updateItm(Map<String, String> param);

	Map<String, String> selectItmInfo(Map<String, String> paramMap);
	
}
