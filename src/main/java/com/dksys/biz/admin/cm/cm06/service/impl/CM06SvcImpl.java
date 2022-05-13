package com.dksys.biz.admin.cm.cm06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm04.mapper.CM04Mapper;
import com.dksys.biz.admin.cm.cm06.mapper.CM06Mapper;
import com.dksys.biz.admin.cm.cm06.service.CM06Svc;

@Service
@Transactional
public class CM06SvcImpl implements CM06Svc {

    @Autowired
    CM06Mapper cm06Mapper;
    
    @Autowired
    CM04Mapper cm04Mapper;

    @Override
	public int selectUserCount(Map<String, String> paramMap) {
		return cm06Mapper.selectUserCount(paramMap);
	}
    
	@Override
	public List<Map<String, String>> selectUserList(Map<String, String> paramMap) {
		return cm06Mapper.selectUserList(paramMap);
	}
	
	@Override
	public Map<String, String> selectUserInfo(Map<String, String> paramMap) {
		return cm06Mapper.selectUserInfo(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectUserTree(Map<String, String> paramMap) {
		List<Map<String, String>> deptTree = cm04Mapper.selectDeptTree(paramMap);
		List<Map<String, String>> useTree = cm06Mapper.selectUserTree(paramMap);
		deptTree.addAll(useTree);
		return deptTree;
	}

	@Override
	public void insertUser(Map<String, String> paramMap) throws Exception{
		cm06Mapper.insertUser(paramMap);
		cm06Mapper.insertUserOauth(paramMap);
	}
	
	@Override
	public void updateUser(Map<String, String> paramMap) throws Exception {
		cm06Mapper.updateUser(paramMap);
	}

	@Override
	public int insertPgmHistory(Map<String, String> paramMap) {
		return cm06Mapper.insertPgmHistory(paramMap);
	}

	@Override
	public int updatePw(Map<String, String> paramMap) {
		int result = 0;
		result += cm06Mapper.updatePw(paramMap);
		result += cm06Mapper.updateTokenPw(paramMap);
		return result;
	}
    
	@Override
	public List<Map<String, String>> selectRuleCheckList(Map<String, String> paramMap) {
		return cm06Mapper.selectRuleCheckList(paramMap); 
	}

	@Override
	public Map<String, String> updatePwErrCnt(Map<String, String> paramMap) {
		cm06Mapper.updatePwErrCnt(paramMap);
		Map<String, String> usrInfo = cm06Mapper.selectUserInfo(paramMap);
		if(Integer.parseInt(usrInfo.get("passErrCnt")) >= 10) {
			cm06Mapper.updateUserN(paramMap);
		}
		return usrInfo;
	}
	
	/* 테스트를 위한 코드이므로 추후 삭제할것. */
	
//	  @Override public void updateUserName() throws Exception{
//	  cm06Mapper.updateUserName(); double testValue = 10 / 0; 
//	  }
	 
}