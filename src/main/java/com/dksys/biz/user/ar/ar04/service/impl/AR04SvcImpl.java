package com.dksys.biz.user.ar.ar04.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar04.mapper.AR04Mapper;
import com.dksys.biz.user.ar.ar04.service.AR04Svc;

@Service
@Transactional("erpTransactionManager")
public class AR04SvcImpl implements AR04Svc {

	@Autowired
    AR02Mapper ar02Mapper;
	
	@Autowired
	AR04Mapper ar04Mapper;
	
	@SuppressWarnings("all")
	@Override
	public int insertBilg(Map<String, Object> paramMap) {
		int result = 0;
		List<String> list = (List<String>) paramMap.get("trstCertiNo");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		Map<String, String> bilgInfo = ar02Mapper.selectBilgInfo(param);
		ar04Mapper.insertBilg(bilgInfo);
		for (String string : list) {
			Map<String, String> sellParam = new HashMap<String, String>();
			sellParam.put("trstCertiNo", string);
			sellParam.put("bilgCertNo", bilgInfo.get("bilgCertNo"));
//			ar02Mapper.updatePchsSell(sellParam);
		}
		return result;
	}
	
}