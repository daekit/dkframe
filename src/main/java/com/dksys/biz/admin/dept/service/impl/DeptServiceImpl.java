package com.dksys.biz.admin.dept.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.dept.mapper.DeptMapper;
import com.dksys.biz.admin.dept.service.DeptService;

@Service
@Transactional
public class DeptServiceImpl implements DeptService {
	
    @Autowired
    DeptMapper deptMapper;

	@Override
	public List<Map<String, String>> selectDeptTree() {
		return deptMapper.selectDeptTree();
	}

	@Override
	public Map<String, String> selectDeptInfo(Map<String, String> paramMap) {
		return deptMapper.selectDeptInfo(paramMap);
	}

	@Override
	public int selectDeptCount(Map<String, String> paramMap) {
		return deptMapper.selectDeptCount(paramMap);
	}

	@Override
	public int updateDept(Map<String, String> paramMap) {
		return deptMapper.updateDept(paramMap);
	}

}