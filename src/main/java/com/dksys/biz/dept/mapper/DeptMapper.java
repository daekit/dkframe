package com.dksys.biz.dept.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper {
	
	List<Map<String, String>> selectDeptTree();
	
}
