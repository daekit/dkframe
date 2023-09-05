package com.dksys.biz.user.rd.rd05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RD05Mapper {

	int selectRcvpayDailyCount(Map<String, String> paramMap);

	List<Map<String, String>> selectRcvpayDailyList(Map<String, String> paramMap);

	int selectRcvpayDailyDtlCount(Map<String, String> paramMap);

	List<Map<String, String>> selectRcvpayDailyDtlList(Map<String, String> paramMap);

	int selectRcvpayDailyOnlyPrdtCount(Map<String, String> param);

	List<Map<String, String>> selectRcvpayDailyListOnlyPrdt(Map<String, String> param);

}