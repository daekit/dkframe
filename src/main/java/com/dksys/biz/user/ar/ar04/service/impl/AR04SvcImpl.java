package com.dksys.biz.user.ar.ar04.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar04.mapper.AR04Mapper;
import com.dksys.biz.user.ar.ar04.service.AR04Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
	public List<Map<String, String>> selectBilgDetailList(Map<String, String> paramMap) {
		return ar04Mapper.selectBilgDetailList(paramMap);
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
		int result = 0;
		result = ar04Mapper.updateTaxBilg(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 세금계산서관리 상세 delete
		ar04Mapper.deleteTaxBilgDetail(paramMap);
		// 세금계산서관리 상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("bilgCertNo", paramMap.get("bilgCertNo"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			if(!detailMap.get("ard113a").isEmpty()) {
				ar04Mapper.insertTaxBilgDetail(detailMap);
			}
		}
		return result;
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
			String orgnTaxBilgNo = "";
			taxHdParam.put("userId", userId);
			taxHdParam.put("userNm", userNm);
			taxHdParam.put("deptId", deptId);
			taxHdParam.put("pgmId", pgmId);
			taxHdParam.put("bilgCertNo", list.get(i).split(",")[0]);
			taxHdParam.put("coCd", list.get(i).split(",")[1]);
			Map<String, String> bilgInfo = new HashMap<String, String>();
			bilgInfo.put("taxBilgNo", "");
			bilgInfo.putAll(ar04Mapper.selectBilgInfo(taxHdParam));
			Map<String, String> temp = new HashMap<String, String>();
			orgnTaxBilgNo = bilgInfo.get("taxBilgNo");
			taxHdParam.put("orgnTaxBilgNo", "");
			if (bilgInfo.get("rffGn1").equals("RFFGN102")
					&& bilgInfo.get("rffAea").equals("RFFAEA01") /* && bilgInfo.get("taxBilgNo") != null */) {
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
				taxHdParam.put("orgnTaxBilgNo", orgnTaxBilgNo);

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
			if(bilgInfo.get("rffGn1").equals("RFFGN101")) {// 계산서 번호가 있고 수정 세금계산서가 아닌경우 기존 BGM1004 사용
				if(bilgInfo.get("taxBilgNo").isEmpty()) {
					bgm1004 = ar04Mapper.selectBgmSeq();
				} else {
					bgm1004 = bilgInfo.get("taxBilgNo");
				}
			} else if(!bilgInfo.get("rffGn1").equals("RFFGN101")) {// 계산서 번호가 없거나 수정 세금계산서인 경우 새로운 BGM1004 따야함
				bgm1004 = ar04Mapper.selectBgmSeq();
			}

			taxHdParam.put("rffGn1", bilgInfo.get("rffGn1"));
			taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
			taxHdParam.put("rffAea", bilgInfo.get("rffAea"));
			
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
	
	@SuppressWarnings("all")
	@Override
	public int insertTaxHdCancel(Map<String, Object> paramMap) {
		int result = 0;
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
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
			
			if(bilgInfo.get("taxAdmsYn").equals("Y")) { //계산서 승인 여부 Y -> 계산서 환입 처리 및 tax, inv 테이블 insert
				//수정세금계산서 취소 로직 시작
				msgId++;
				
				bgm1004 = ar04Mapper.selectBgmSeq();
				xmlMsgId = ar04Mapper.selectMsgId(msgId);
				taxHdParam.put("docCode", "938"); //세금계산서 938
				taxHdParam.put("bgm1004", bgm1004);
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("taxBilgNo", bilgInfo.get("taxBilgNo"));
				taxHdParam.put("rffGn1", "RFFGN102");
				taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
				taxHdParam.put("rffAea", "RFFAEA03");
				result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
				ar04Mapper.insertTaxHdCancel(taxHdParam); // tax bilg no 으로 bgm1004를 찾아서 금액을 -1 곱한 tax hd 를 insert
				ar04Mapper.insertTaxDtl(taxHdParam);
				result = ar04Mapper.insertTaxItemCancel(taxHdParam);
				ar04Mapper.updateTaxBilgNoCancel(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서, 수정사유코드 : 환입
				
				//수정거래명세서 취소 로직 시작
				msgId++; //XML_MSG_ID 생성
				xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("docCode", "780"); // 거래명세서 780
				result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
				result = ar04Mapper.insertInvHdCancel(taxHdParam); // 거래명세서용 inv Hd insert
				result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
				result = ar04Mapper.insertInvItemCancel(taxHdParam); // 거래명세서용 inv item insert
			} else { // 계산서 승인 전 -> bgm1225 를 3으로 계산서 발행
				//수정세금계산서 "삭제" 로직 시작
				msgId++;
				
				//bgm1004 = ar04Mapper.selectBgmSeq();
				xmlMsgId = ar04Mapper.selectMsgId(msgId);
				taxHdParam.put("docCode", "938"); //세금계산서 938
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("taxBilgNo", bilgInfo.get("taxBilgNo"));
				bgm1004 = bilgInfo.get("taxBilgNo");
				taxHdParam.put("bgm1004", bgm1004);
				taxHdParam.put("bgm1225", "3");
				String bgm4343 = ar04Mapper.selectBgm4343(bilgInfo.get("taxBilgNo"));// 삭제의 경우 전송회차 증가 
				taxHdParam.put("bgm4343", bgm4343);
				result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
				ar04Mapper.insertTaxHdDelete(taxHdParam); // tax bilg no 으로 bgm1004를 찾아서 금액을 -1 곱한 tax hd 를 insert
				ar04Mapper.insertTaxDtl(taxHdParam);
				result = ar04Mapper.insertTaxItemDelete(taxHdParam);
				ar04Mapper.updateTaxBilgNoDelete(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서, 수정사유코드 : 환입
				
				//수정거래명세서 취소 로직 시작
				msgId++; //XML_MSG_ID 생성
				xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
				taxHdParam.put("xmlMsgId", xmlMsgId);
				taxHdParam.put("docCode", "780"); // 거래명세서 780
				result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
				result = ar04Mapper.insertInvHdDelete(taxHdParam); // 거래명세서용 inv Hd insert
				result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
				result = ar04Mapper.insertInvItemDelete(taxHdParam); // 거래명세서용 inv item insert
			}
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
	
	@Override
	@SuppressWarnings("all")
	public int updateBilgRvrs(Map<String, Object> paramMap) {

		int result = 0;
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		//List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, String> param = new HashMap<String, String>();
		int msgId = 0;
		for(int i = 0; i < list.size(); i++) {
			param.put("userId", userId);
			param.put("userNm", userNm);
			param.put("deptId", deptId);
			param.put("pgmId", pgmId);
			param.put("bilgCertNo", list.get(i).split(",")[0]);
			param.put("coCd", list.get(i).split(",")[1]);
			result = ar04Mapper.updateBilgRvrs(param); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서, 수정사유코드 : 환입
		}
		return result;
	}
	
	
}