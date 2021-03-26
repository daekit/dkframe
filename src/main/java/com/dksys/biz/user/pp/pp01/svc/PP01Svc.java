package com.dksys.biz.user.pp.pp01.svc;

import java.util.List;
import java.util.Map;

public interface PP01Svc {

	int selectMesOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap);

}