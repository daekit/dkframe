package com.dksys.biz.user.ar.ar13.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface AR13Svc {

	int selectPrjtMngTernKeyCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPrjtMngTernKey(Map<String, String> paramMap);
	
}