package com.dksys.biz.user.od.od02.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface OD02Svc {

	int selectPurchaseCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPurchaseList(Map<String, String> paramMap);

}