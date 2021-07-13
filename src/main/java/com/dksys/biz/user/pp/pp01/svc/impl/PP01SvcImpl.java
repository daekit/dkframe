package com.dksys.biz.user.pp.pp01.svc.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.pp.pp01.mapper.PP01Mapper;
import com.dksys.biz.user.pp.pp01.svc.PP01Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class PP01SvcImpl implements PP01Svc {

    @Autowired
    PP01Mapper pp01Mapper;
    
    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public int selectMesOrderCount(Map<String, String> paramMap) {
		return pp01Mapper.selectMesOrderCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesOrderList(Map<String, String> paramMap) {
		return pp01Mapper.selectMesOrderList(paramMap);
	}

	@Override
	public Map<String, Object> selectMesOrderDetail(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("reqDt")+paramMap.get("shipSeq")));
		returnMap.put("mesOrderDetail", pp01Mapper.selectMesOrderDetail(paramMap));
		returnMap.put("mesOrderDetailList", pp01Mapper.selectOrderDetailList(paramMap));
		return returnMap;
	}
    
	@Override
	public int insertMesOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = 0;

		result = pp01Mapper.insertOrder(paramMap);
		result = pp01Mapper.insertMesOrder(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 출하상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("worksCd", paramMap.get("worksCd"));
			detailMap.put("mesOrdNo", paramMap.get("mesOrdNo"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			result = pp01Mapper.insertOrderDetail(detailMap);
			result = pp01Mapper.insertMesOrderDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
		
		pp01Mapper.callPL_SD04_MES();
		return result;
	}
	
}