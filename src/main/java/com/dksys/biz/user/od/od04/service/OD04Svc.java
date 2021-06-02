package com.dksys.biz.user.od.od04.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface OD04Svc {

	public int selectReqListCount(Map<String, String> param);

	public List<Map<String, String>> selectReqList(Map<String, String> param);

	public Map<String, Object> selectReq(Map<String, String> param);

	public Map<String, Object> selectReqOrder(Map<String, Object> param);

	public List<Map<String, String>> selectReqDetail(Map<String, String> param);

	int insertReq(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int deleteReq(Map<String, String> paramMap);
	
	int updateReq(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	int updateRecptList(Map<String, Object> paramMap);
	
	int updateRecpt(Map<String, String> paramMap);
}