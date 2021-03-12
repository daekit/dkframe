package com.dksys.biz.user.od.od02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OD02Mapper {

	int selectPurchaseCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPurchaseList(Map<String, String> paramMap);

}