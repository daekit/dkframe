package com.dksys.biz.user.ifsys.ifar01.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.user.ifsys.ifar01.service.IFAR01Svc;
import com.dksys.biz.user.ifsys.mes.mapper.MESSTOCKMapper;
import com.dksys.biz.user.ifsys.mes.service.MESSTOCKSvc;
import com.dksys.biz.util.ExceptionThrower;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class IFAR01SvcImpl implements IFAR01Svc {
	
    @Autowired
    MESSTOCKMapper messtockMapper;
    
    
    @Autowired
    ExceptionThrower thrower;
    
//    @Override
//	public int insertIfMesStockIn(Map<String, String> paramMap) {
//		return mesStockMapper.insertIfMesStockIn(paramMap);
//	}


}