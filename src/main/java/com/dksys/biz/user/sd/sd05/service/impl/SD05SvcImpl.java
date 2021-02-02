package com.dksys.biz.user.sd.sd05.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd05.mapper.SD05Mapper;
import com.dksys.biz.user.sd.sd05.service.SD05Svc;

@Service
@Transactional("erpTransactionManager")
public class SD05SvcImpl implements SD05Svc {
	
    @Autowired
    SD05Mapper sd05Mapper;

	@Override
	public List<Map<String, String>> selectProjectList(Map<String, String> param) {
		return sd05Mapper.selectProjectList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectcoCdList(Map<String, String> param) {
		return sd05Mapper.selectProjectcoCdList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectNameList(Map<String, String> param) {
		return sd05Mapper.selectProjectNameList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectClntList(Map<String, String> param) {
		return sd05Mapper.selectProjectClntList(param);
	}
	
	@Override
	public Map<String, String> selectProjectKeyList(Map<String, String> param) {
		return sd05Mapper.selectProjectKeyList(param);
	}
	
	@Override
	public int selectProjectCount(Map<String, String> param) {
		return sd05Mapper.selectProjectCount(param);
	}
	
	@Override
	public int insertProject(Map<String, String> param) {
		return sd05Mapper.insertProject(param);
	}
}