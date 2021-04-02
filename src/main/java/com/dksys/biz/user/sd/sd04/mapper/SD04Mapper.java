package com.dksys.biz.user.sd.sd04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD04Mapper {

	int selectOrderCount(Map<String, String> paramMap);

	List<Map<String, Object>> selectOrderList(Map<String, String> paramMap);
	
	Map<String, Object> selectOrderInfo(Map<String, String> paramMap);
	
	int selectFixedOrderCount(Map<String, String> paramMap);
	
	int insertOrder(Map<String, String> paramMap);

	int insertOrderDetail(Map<String, String> detailMap);
	
	int updateOrder(Map<String, String> paramMap);

	int deleteOrder(Map<String, String> paramMap);
	
	int deleteOrderDetail(Map<String, String> paramMap);

	int closeOrder(Map<String, String> paramMap);

	int closeOrderDetail(Map<String, String> paramMap);

	String selectOdrSeq();

}