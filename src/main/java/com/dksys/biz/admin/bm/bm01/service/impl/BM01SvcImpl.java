package com.dksys.biz.admin.bm.bm01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.admin.bm.bm01.service.BM01Svc;

@Service
public class BM01SvcImpl implements BM01Svc {
	
    @Autowired
    BM01Mapper bm01Mapper;
    
    @Override
	public int selectMaterialCount(Map<String, String> param) {
		return bm01Mapper.selectMaterialCount(param);
	}

	@Override
	public List<Map<String, String>> selectMaterialList(Map<String, String> param) {
		return bm01Mapper.selectMaterialList(param);
	}
	
	@Override
	public Map<String, String> selectMaterialInfo(Map<String, String> param) {
		return bm01Mapper.selectMaterialInfo(param);
	}
	
	@Override
	public int checkOverLap(Map<String, String> param) {
		return bm01Mapper.checkOverLap(param);
	}

	@Override
	public int insertMaterial(Map<String, String> param) {
		return bm01Mapper.insertMaterial(param);
	}

	@Override
	public int updateMaterial(Map<String, String> param) {
		return bm01Mapper.updateMaterial(param);
	}
	
	@Override
	public int deleteMaterial(Map<String, String> param) {
		return bm01Mapper.deleteMaterial(param);
	}

}