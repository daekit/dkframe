package com.dksys.biz.user.pp.pp01.svc;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface PP01Svc {

	int selectMesOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap);

	Map<String, Object> selectMesOrderDetail(Map<String, String> paramMap);
	
	int insertMesOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

}