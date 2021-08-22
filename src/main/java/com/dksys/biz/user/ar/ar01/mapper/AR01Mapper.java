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

	Map<String, Object> selectShipInfo(Map<String, String> paramMap);
	
	Map<String, String> getOrderInfo(Map<String, Object> paramMap);

	List<Map<String, String>> selectShipDetail(Map<String, String> paramMap);
	
	List<Map<String, String>> getOrderDetail(Map<String, Object> paramMap);

	int updateShip(Map<String, String> paramMap);
	
	int updateSalesMng(Map<String, String> paramMap);
	
	int updateShipRmk(Map<String, String> paramMap);

	int updateConfirm(Map<String, String> paramMap);

	int updateConfirmDetail(Map<String, String> detailMap);

	Map<String, String> selectShipDetailInfo(Map<String, String> detailMap);

	int selectConfirmCount(Map<String, String> paramMap);

	int selectDetailCount(Map<String, String> paramMap);

	int updateCancelDetail(Map<String, String> detailMap);

	int updateCancel(Map<String, String> paramMap);

	int updateRecptYn(Map<String, String> detailMap);

	void updateTrans(Map<String, String> paramMap);
}