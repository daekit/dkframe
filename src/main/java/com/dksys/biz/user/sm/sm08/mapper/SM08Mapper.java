package com.dksys.biz.user.sm.sm08.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM08Mapper {
	
	int selectRcvpayTnKeyDailyListCount(Map<String, String> param);
	
	List<Map<String, String>> selectRcvpayTnKeyDailyList(Map<String, String> param);
	

}
