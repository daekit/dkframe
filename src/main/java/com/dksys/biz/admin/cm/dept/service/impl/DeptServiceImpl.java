package com.dksys.biz.admin.cm.dept.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.dept.mapper.DeptMapper;
import com.dksys.biz.admin.cm.dept.service.DeptService;

@Service
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
	public void updateDept(Map<String, String> paramMap) throws Exception{
		deptMapper.updateDept(paramMap);
	}

	@Override
	public void moveDept(List<Map<String, String>> paramList) throws Exception{
		for(Map<String, String> paramMap : paramList) {
			deptMapper.moveDept(paramMap);
		}
	}

}