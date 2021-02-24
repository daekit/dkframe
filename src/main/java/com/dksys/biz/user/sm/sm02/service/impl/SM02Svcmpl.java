package com.dksys.biz.user.sm.sm02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sm.sm02.mapper.SM02Mapper;
import com.dksys.biz.user.sm.sm02.service.SM02Svc;

@Service
public class SM02Svcmpl implements SM02Svc {
	
    @Autowired
    SM02Mapper sm02Mapper;

    
	@Override
	public List<Map<String, String>> selectCmnCodeList(Map<String, String> param) {
		return sm02Mapper.selectCmnCodeList(param);
	}
	
	@Override
	public List<Map<String, String>> selectDtlCmnCodeList(Map<String, String> param) {
		return sm02Mapper.selectDtlCmnCodeList(param);
	}

	@Override
	public List<Map<String, String>> selectClntSearchList(Map<String, String> param) {
		return sm02Mapper.selectClntSearchList(param);
	}

	@Override
	public List<Map<String, String>> selectPrdtSearchList(Map<String, String> param) {
		return sm02Mapper.selectPrdtSearchList(param);
	}


	@Override
	public int selectUprCount(Map<String, String> param) {
		return sm02Mapper.selectUprCount(param);
	}


	@Override
	public List<Map<String, String>> selectStockMoveStatMngmList(Map<String, String> param) {
		return sm02Mapper.selectStockMoveStatMngmList(param);
	}
	
	@Override
	public int selectUprDtlCount(Map<String, String> param) {
		return sm02Mapper.selectUprDtlCount(param);
	}
	
	@Override
	public List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param) {
		return sm02Mapper.selectStockMoveStatMngmDtlList(param);
	}
	
	@Override
	public int sm01CheckCnt(Map<String, String> param) {
		return sm02Mapper.sm01CheckCnt(param);
	}

	@Override
	public int sm01InsertStockMove(Map<String, String> param) {
		return sm02Mapper.sm01InsertStockMove(param);
	}

	@Override
	public int sm01UpdateStockMove(Map<String, String> param) {
		return sm02Mapper.sm01UpdateStockMove(param);
	}
	
	@Override
	public int sm01UpdateInsertStockMove(Map<String, String> param) {
		return sm02Mapper.sm01UpdateInsertStockMove(param);
	}

	@Override
	public int sm02InsertStockMove(Map<String, String> param) {
		return sm02Mapper.sm02InsertStockMove(param);
	}
	
}