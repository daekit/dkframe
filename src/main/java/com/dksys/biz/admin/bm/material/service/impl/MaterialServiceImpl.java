package com.dksys.biz.admin.bm.material.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.material.mapper.MaterialMapper;
import com.dksys.biz.admin.bm.material.service.MaterialService;

@Service
@Transactional("erpTransactionManager")
public class MaterialServiceImpl implements MaterialService {
	
    @Autowired
    MaterialMapper materialMapper;

	@Override
	public List<Map<String, String>> selectMaterialList() {
		return materialMapper.selectMaterialList();
	}

}