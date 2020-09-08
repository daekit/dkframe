package com.dksys.biz.admin.dept.service;

import java.util.List;
import java.util.Map;

public interface DeptService {
	
	List<Map<String, String>> selectDeptTree();

	Map<String, String> selectDeptInfo(Map<String, String> paramMap);

	int selectDeptCount(Map<String, String> paramMap);

	int updateDept(Map<String, String> paramMap);
	
}