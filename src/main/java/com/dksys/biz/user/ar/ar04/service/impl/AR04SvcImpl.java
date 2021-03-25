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
	
	@Override
	public int selectTaxBilgCount(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgCount(paramMap);
	}
	
	@Override
	public int selectTaxBilgDetailCount(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgDetailCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxBilgList(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxBilgDetailList(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgDetailList(paramMap);
	}

	@Override
	public Map<String, String> selectTaxBilg(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilg(paramMap);
	}
    
	@Override
	public int updateTaxBilg(Map<String, String> paramMap) {
		return ar04Mapper.updateTaxBilg(paramMap);
	}
    
	@Override
	public int selectTaxRcvCount(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxRcvCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxRcvList(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxRcvList(paramMap);
	}
	
	@SuppressWarnings("all")
	@Override
	public int insertTaxHd(Map<String, Object> paramMap) {
		int result = 0;
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		//List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		for(int i = 0; i < list.size(); i++) {
			int msgId = i + 1;
			String xmlMsgId = "";
			Map<String, String> taxHdParam = new HashMap<String, String>();
			taxHdParam.put("msgId", Integer.toString(msgId));
			xmlMsgId = ar04Mapper.selectMsgId(msgId);
			String bgm1004 = ar04Mapper.selectBgmSeq();
			taxHdParam.put("userId", userId);
			taxHdParam.put("userNm", userNm);
			taxHdParam.put("pgmId", pgmId);
			taxHdParam.put("bgm1004", bgm1004);
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("bilgCertNo", list.get(i).split(",")[0]);
			taxHdParam.put("coCd", list.get(i).split(",")[1]);
			
			result = ar04Mapper.insertTaxHd(taxHdParam); // taxHd insert
			ar04Mapper.updateTaxBilgNo(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트

			result = ar04Mapper.insertTaxItem(taxHdParam);
		}
		return result;
	}
}