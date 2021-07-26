package com.dksys.biz.user.od.od01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OD01Mapper {
	
	int selectOrdrgCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdrgList(Map<String, String> paramMap);
	
	Map<String, String> selectOrdrgInfo(Map<String, String> paramMap);
	
	int selectOrdrgDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdrgDetailList(Map<String, String> paramMap);
	
	Map<String, String> selectOrdrgDetailInfo(Map<String, String> detailMap);
	
	Map<String, String> getOrderInfo(Map<String, Object> paramMap);
	
	List<Map<String, String>> getOrderDetailList(Map<String, Object> paramMap);
	
	int insertOrdrg(Map<String, String> paramMap);
	
	int insertOrdrgDetail(Map<String, String> detailMap);
	
	int updateOrdrg(Map<String, String> paramMap);
	
	int updateOrdrgDetail(Map<String, String> paramMap);
	
	int updateSalesMng(Map<String, String> paramMap);
	
	int updateOrdrgRmk(Map<String, String> paramMap);
	
	int deleteOrdrg(Map<String, String> paramMap);
	
	int deleteOrdrgDetail(Map<String, String> paramMap);
	
	int selectDetailCount(Map<String, String> paramMap);
	
	// 매입관련 업무
    // -----------------------------------------------------------	
	int selectConfirmCount(Map<String, String> paramMap);
	
	int updateConfirm(Map<String, String> paramMap);

	int updateConfirmDetail(Map<String, String> detailMap);
	
	int updateCancel(Map<String, String> paramMap);

	int updateCancelDetail(Map<String, String> detailMap);
	
    // 매출관련 업무
    // -----------------------------------------------------------	
	int selectConfirmCountS(Map<String, String> paramMap);
	
	int updateConfirmS(Map<String, String> paramMap);

	int updateConfirmDetailS(Map<String, String> detailMap);
	
	int updateCancelS(Map<String, String> paramMap);

	int updateCancelDetailS(Map<String, String> detailMap);

}