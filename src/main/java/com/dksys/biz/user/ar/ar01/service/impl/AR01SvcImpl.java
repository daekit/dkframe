package com.dksys.biz.user.ar.ar01.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.ar.ar01.mapper.AR01Mapper;
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class AR01SvcImpl implements AR01Svc {
    
    @Autowired
    AR01Mapper ar01Mapper;
    
    @Autowired
    AR01Svc ar01Svc;

    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public int insertShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = ar01Mapper.insertShip(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		ar01Mapper.deleteShipDetail(paramMap);
		// 발주상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("shipSeq", paramMap.get("shipSeq"));
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("ordDtlSeq", paramMap.get("ordDtlSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			ar01Mapper.insertShipDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_AR01M01", paramMap.get("reqDt")+paramMap.get("shipSeq"), mRequest);
		return result;
	}

	@Override
	public int selectShipCount(Map<String, String> paramMap) {
		return ar01Mapper.selectShipCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectShipList(Map<String, String> paramMap) {
		return ar01Mapper.selectShipList(paramMap);
	}

	@Override
	public int deleteShip(Map<String, String> paramMap) {
		return ar01Mapper.deleteShip(paramMap);
	}
    

}