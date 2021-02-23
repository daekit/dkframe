package com.dksys.biz.user.ar.ar01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR01Mapper {

	int insertShip(Map<String, String> paramMap);

	int deleteShipDetail(Map<String, String> paramMap);

	int insertShipDetail(Map<String, String> detailMap);

	int selectShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipList(Map<String, String> paramMap);

	int deleteShip(Map<String, String> paramMap);
	
}