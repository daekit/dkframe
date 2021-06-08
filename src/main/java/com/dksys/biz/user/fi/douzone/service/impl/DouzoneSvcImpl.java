package com.dksys.biz.user.fi.douzone.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.fi.douzone.mapper.DouzoneMapper;
import com.dksys.biz.user.fi.douzone.service.DouzoneSvc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class DouzoneSvcImpl implements DouzoneSvc {
	
    @Autowired
    DouzoneMapper douzoneMapper;
    
	@Override
	public int insertAutodocuSimple(Map<String, String> paramMap) {
		return douzoneMapper.insertAutodocuSimple(paramMap);
	}

	@Override
	public int updateAutodocuSimple(Map<String, String> paramMap) {
		return douzoneMapper.updateAutodocuSimple(paramMap);
	}

	@Override
	public int deleteAutodocuSimple(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> saveList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for (Map<String, String> saveMap : saveList) {
			result = douzoneMapper.deleteAutodocuSimple(saveMap);
		}
		return result;
	}
}