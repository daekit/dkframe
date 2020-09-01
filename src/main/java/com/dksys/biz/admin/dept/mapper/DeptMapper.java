package com.dksys.biz.admin.dept.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper {
	
	List<Map<String, String>> selectDeptTree();

	Map<String, String> selectDeptInfo(HashMap<String, String> paramMap);
	
}
