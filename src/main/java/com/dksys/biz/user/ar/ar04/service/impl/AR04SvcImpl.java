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
		String deptId = String.valueOf(paramMap.get("deptId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		//List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		int msgId = 0;
		for(int i = 0; i < list.size(); i++) {
			//
			String xmlMsgId = "";
			Map<String, String> taxHdParam = new HashMap<String, String>();
			taxHdParam.put("msgId", Integer.toString(msgId));
			String bgm1004 = "";
			
			taxHdParam.put("userId", userId);
			taxHdParam.put("userNm", userNm);
			taxHdParam.put("deptId", deptId);
			taxHdParam.put("pgmId", pgmId);
			taxHdParam.put("bilgCertNo", list.get(i).split(",")[0]);
			taxHdParam.put("coCd", list.get(i).split(",")[1]);
			Map<String, String> bilgInfo = ar04Mapper.selectBilgInfo(taxHdParam);
			if (!bilgInfo.get("rffGn1").equals("RFFGN101")
					&& !bilgInfo.get("rffAea").equals("RFFAEA00") /* && bilgInfo.get("taxBilgNo") != null */) {
				//수정세금계산서 취소 로직 시작
				msgId++;
				
				bgm1004 = ar04Mapper.selectBgmSeq();
				xmlMsgId = ar04Mapper.selectMsgId(msgId);
				taxHdParam.put("docCode", "938"); //세금계산서 938
				taxHdParam.put("bgm1004", bgm1004);
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("taxBilgNo", bilgInfo.get("taxBilgNo"));
				taxHdParam.put("rffGn1", bilgInfo.get("rffGn1"));
				taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
				taxHdParam.put("rffAea", bilgInfo.get("rffAea"));

				result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
				ar04Mapper.insertTaxHdCancel(taxHdParam); // tax bilg no 으로 bgm1004를 찾아서 금액을 -1 곱한 tax hd 를 insert
				ar04Mapper.insertTaxDtl(taxHdParam);
				result = ar04Mapper.insertTaxItemCancel(taxHdParam);
				
				
				
				//수정거래명세서 취소 로직 시작

				msgId++; //XML_MSG_ID 생성
				xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("docCode", "780"); // 거래명세서 780
				result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
				result = ar04Mapper.insertInvHdCancel(taxHdParam); // 거래명세서용 inv Hd insert
				result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
				result = ar04Mapper.insertInvItemCancel(taxHdParam); // 거래명세서용 inv item insert
			}
			

			msgId++;
			// 계산서 번호가 있고 수정 세금계산서가 아닌경우 기존 BGM1004 사용
			if(bilgInfo.get("rffGn1").equals("RFFGN101")) {
				bgm1004 = bilgInfo.get("taxBilgNo");
			} else if(!bilgInfo.get("rffGn1").equals("RFFGN101")) {
				bgm1004 = ar04Mapper.selectBgmSeq();
			}
			// 계산서 번호가 없거나 수정 세금계산서인 경우 새로운 BGM1004 따야함
			
			xmlMsgId = ar04Mapper.selectMsgId(msgId);
			taxHdParam.put("bgm1004", bgm1004);
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docCode", "938"); //세금계산서 938
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
			result = ar04Mapper.insertTaxHd(taxHdParam); // taxHd insert
			ar04Mapper.updateTaxBilgNo(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트
			result = ar04Mapper.insertTaxDtl(taxHdParam);
			result = ar04Mapper.insertTaxItem(taxHdParam);
			
			// 거래명세서 발행
			msgId++; //XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
			result = ar04Mapper.insertInvHd(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertItem(taxHdParam); // 거래명세서용 inv item insert
			
			
		}
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public int updateBilgCancel(Map<String, Object> paramMap) {
		int result = 0;
		List<String> list = (List<String>) paramMap.get("bilgCertNo");
		Map<String, String> param = new HashMap<String, String>();
		param.put("bilgCertNo", list.get(0));
		Map<String, String> bilgInfo = ar04Mapper.selectTaxBilg(param);
		if(bilgInfo.get("taxBilgNo") == null) {
			result = ar04Mapper.deleteBilgInfo(param);
			ar02Mapper.updateBilgCancel(param);
		}
		return result;
	}
}