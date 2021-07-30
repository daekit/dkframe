package com.dksys.biz.user.sm.sm10.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM10Mapper {
	
	int reCalcStockUpdate(Map<String, String> paramMap);

	int reCalcStockTemp(Map<String, String> paramMap);

	int reCalcStockMerge(Map<String, String> paramMap);
	
	int reCalcStockDeleteTemp(Map<String, String> paramMap);

	

}
