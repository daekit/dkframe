package com.dksys.biz.user.od.od04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OD04Mapper {

	int selectReqListCount(Map<String, String> param);
	
	List<Map<String, String>> selectReqList(Map<String, String> param);

	Map<String, String> selectReq(Map<String, String> param);

	List<Map<String, String>> selectReqDetail(Map<String, String> param);

	Map<String, String> selectReqOrder(Map<String, Object> param);

	List<Map<String, String>> selectReqOrderDetail(Map<String, Object> param);

	int insertReq(Map<String, String> paramMap);

	int updateReq(Map<String, String> paramMap);

	int updateRecpt(String repSeq);

	int deleteReq(Map<String, String> paramMap);

	int deleteReqDetail(Map<String, String> paramMap);

	int insertReqDetail(Map<String, String> detailMap);

	int updateReqOrder(Map<String, String> paramMap);
	
	int selectOrdrgNCnt(Map<String, String> paramMap);

	int updateReqOrdrgY(Map<String, String> paramMap);
}
