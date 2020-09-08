package com.dksys.biz.admin.dept.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DeptService {
	
	List<Map<String, String>> selectDeptTree();

	Map<String, String> selectDeptInfo(HashMap<String, String> paramMap);

	int selectDeptCount(HashMap<String, String> paramMap);
	
}