package com.dksys.biz.dept.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.dept.mapper.DeptMapper;
import com.dksys.biz.dept.service.DeptService;

@Service
@Transactional
public class DeptServiceImpl implements DeptService {
	
    @Autowired
    DeptMapper deptMapper;

	@Override
	public List<Map<String, String>> selectDeptTree() {
		List<Map<String, String>> testList = deptMapper.selectDeptTree();
		return testList;
	}

}