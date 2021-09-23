package com.dksys.biz.user.ar.ar03.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar01.mapper.AR01Mapper;
import com.dksys.biz.user.ar.ar03.mapper.AR03Mapper;
import com.dksys.biz.user.ar.ar03.service.AR03Svc;
import com.dksys.biz.user.od.od01.mapper.OD01Mapper;
import com.dksys.biz.user.sm.sm02.mapper.SM02Mapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR03SvcImpl implements AR03Svc {

	@Autowired
    AR03Mapper ar03Mapper;
	
	@Autowired
    AR01Mapper ar01Mapper;

	@Autowired
    OD01Mapper od01Mapper;
	
	@Autowired
    SM02Mapper sm02Mapper;
	
	@Override
	public int selectCaryngCount(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCaryngList(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngList(paramMap);
	}
	
	@Override
	public Map<String, String> selectCaryngInfo(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngInfo(paramMap);
	}
	
	@Override
	public int selectShipCount(Map<String, String> paramMap) {
		return ar03Mapper.selectShipCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectShipList(Map<String, String> paramMap) {
		return ar03Mapper.selectShipList(paramMap);
	}
	
	@Override
	public int insertCaryng(Map<String, String> param) {
		return ar03Mapper.insertCaryng(param);
	}

	@Override
	public int updateCaryng(Map<String, String> param) {
		return ar03Mapper.updateCaryng(param);
	}

	@Override
	public int deleteTrans(Map<String, String> paramMap) {
	       ar01Mapper.updateTrans(paramMap);  // 출하지시에서 제거
	       od01Mapper.updateTrans(paramMap);  // 발주서에서 제거
	       sm02Mapper.updateTrans(paramMap);  // 재고이동에서 제거
    	return ar03Mapper.deleteTrans(paramMap);
	}

	@Override
	@SuppressWarnings("all")
	public int updateProcYn(Map<String, Object> param) {
		int result = 0;
		List<Map<String, String>> detailList = (List<Map<String, String>>) param.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += ar03Mapper.updateProcYn(detailMap);
		}
		return result;
	}

}