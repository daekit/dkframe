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
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		List<String> list = (List<String>) paramMap.get("trstCertiNo");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("userId", userId);
		param.put("userNm", userNm);
		param.put("pgmId", pgmId);
		Map<String, String> bilgInfo = ar02Mapper.selectBilgInfo(param);
		// bilgInfo: CamelMap이라 대문자 형태로 SET 해야함
		int bilgCertNo = ar04Mapper.getBilgCertNo();
		bilgInfo.put("USER_ID", userId);
		bilgInfo.put("PGM_ID", pgmId);
		bilgInfo.put("BILG_CERT_NO", String.valueOf(bilgCertNo));
		result = ar04Mapper.insertBilg(bilgInfo);
		for (String trstCertiNo : list) {
			Map<String, String> sellParam = new HashMap<String, String>();
			sellParam.put("trstCertiNo", trstCertiNo);
			sellParam.put("bilgCertNo", bilgInfo.get("bilgCertNo"));
			ar02Mapper.updatePchsSellBilg(sellParam);
		}
		return result;
	}
	
}