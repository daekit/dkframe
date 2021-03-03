package com.dksys.biz.user.od.od01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OD01Mapper {

	int insertOrder(Map<String, String> paramMap);

	int selectOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectOrderList(Map<String, String> paramMap);

	int deleteOrderDetail(Map<String, String> paramMap);

	int insertOrderDetail(Map<String, String> detailMap);

	int deleteOrder(Map<String, String> paramMap);

	Map<String, String> selectOrderInfo(Map<String, String> paramMap);
	
	Map<String, String> getOrderInfo(Map<String, Object> paramMap);

	List<Map<String, String>> selectOrderDetail(Map<String, String> paramMap);
	
	List<Map<String, String>> getOrderDetail(Map<String, Object> paramMap);

	int updateOrder(Map<String, String> paramMap);

	int updateConfirm(Map<String, String> paramMap);

	int updateConfirmDetail(Map<String, String> detailMap);

	Map<String, String> selectOrderDetailInfo(Map<String, String> detailMap);

	int selectDetailCount(Map<String, String> paramMap);

	int selectConfirmCount(Map<String, String> paramMap);
	
}