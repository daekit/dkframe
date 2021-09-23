package com.dksys.biz.admin.bm.material.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.material.mapper.MaterialMapper;
import com.dksys.biz.admin.bm.material.service.MaterialService;

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialServiceImpl implements MaterialService {
	
    @Autowired
    MaterialMapper materialMapper;

	@Override
	public List<Map<String, String>> selectMaterialList(Map<String, String> param) {
		return materialMapper.selectMaterialList(param);
	}

	@Override
	public int deleteMaterial(Map<String, String> param) {
		return materialMapper.deleteMaterial(param);
	}

	@Override
	public int insertMaterial(Map<String, String> param) {
		return materialMapper.insertMaterial(param);
	}

	@Override
	public int checkOverLap(Map<String, String> param) {
		return materialMapper.checkOverLap(param);
	}

	@Override
	public Map<String, String> selectMaterialInfo(Map<String, String> param) {
		return materialMapper.selectMaterialInfo(param);
	}

	@Override
	public int selectMaterialCount(Map<String, String> param) {
		return materialMapper.selectMaterialCount(param);
	}

}