package com.dksys.biz.user.fi.fi01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.fi.fi01.mapper.FI01Mapper;
import com.dksys.biz.user.fi.fi01.service.FI01Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class FI01SvcImpl implements FI01Svc {
	
    @Autowired
    FI01Mapper fi01Mapper;
    
	@Override
	public int selectPrftDeptCount(Map<String, String> paramMap) {
		return fi01Mapper.selectPrftDeptCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPrftDeptList(Map<String, String> paramMap) {
		return fi01Mapper.selectPrftDeptList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectUpperDept(Map<String, String> paramMap) {
		return fi01Mapper.selectUpperDept(paramMap);
	}

	@Override
	public List<Map<String, String>> selecrLastDept(Map<String, String> paramMap) {
		return fi01Mapper.selecrLastDept(paramMap);
	}

	@Override
	public Map<String, String> selectPrftDeptDetail(Map<String, String> paramMap) {
		return fi01Mapper.selectPrftDeptDetail(paramMap);
	}
    
	@Override
	public int selectPrdtDeptDuplicate(Map<String, String> paramMap) {
		return fi01Mapper.selectPrdtDeptDuplicate(paramMap);
	}

	@Override
	public int insertPrftDept(Map<String, String> paramMap) {
		return fi01Mapper.insertPrftDept(paramMap);
	}

	@Override
	public int updatePrftDept(Map<String, String> paramMap) {
		return fi01Mapper.updatePrftDept(paramMap);
	}

	@Override
	public int deletePrftDept(Map<String, String> paramMap) {
		return fi01Mapper.deletePrftDept(paramMap);
	}
}