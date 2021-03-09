package com.dksys.biz.user.rd.rd04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RD04Mapper {

	int selectRcvpayCount(Map<String, String> paramMap);

	List<Map<String, String>> selectRcvpayList(Map<String, String> paramMap);

}