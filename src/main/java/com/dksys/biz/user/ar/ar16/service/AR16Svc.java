package com.dksys.biz.user.ar.ar16.service;

import java.util.List;
import java.util.Map;

public interface AR16Svc {

	int selectLoanAmtCount(Map<String, String> paramMap);

	List<Map<String, String>> selectLoanAmtList(Map<String, String> paramMap);

	

	
}
