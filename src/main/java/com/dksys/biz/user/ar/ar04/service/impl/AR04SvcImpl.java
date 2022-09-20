package com.dksys.biz.user.ar.ar04.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.config.YamlReader;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar04.mapper.AR04Mapper;
import com.dksys.biz.user.ar.ar04.service.AR04Svc;
import com.dksys.biz.user.ar.ar05.mapper.AR05Mapper;
import com.dksys.biz.user.ar.ar06.mapper.AR06Mapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR04SvcImpl implements AR04Svc {

	@Autowired
	AR02Mapper ar02Mapper;

	@Autowired
	AR04Mapper ar04Mapper;

	@Autowired
	AR05Mapper ar05Mapper;

	@Autowired
	AR06Mapper ar06Mapper;
	
	@Autowired
	YamlReader yamlReader;

	@SuppressWarnings("all")
	@Override
	public void insertBilg(Map<String, Object> paramMap) throws Exception {

		Map<String, String> bilgInfo = ar02Mapper.selectBilgInfo(paramMap);
		String bilgCertNo = String.valueOf(ar04Mapper.getBilgCertNo());
		
		// bilgInfo: CamelMap이라 대문자 형태로 SET
		bilgInfo.put("USER_ID", paramMap.get("userId").toString());
		bilgInfo.put("PGM_ID", paramMap.get("pgmId").toString());
		bilgInfo.put("BILG_CERT_NO", bilgCertNo);

		// 매출리스트에서 매출확정을 두번 클릭했을 때 한번만 INSERT 되도록 로직 수정
		// 현재 선택된 매출리스트에 세금계산서 번호 (BILG_CERT_NO)가 있는지 확인하고, 있으면 에러로 떨군다.
		List<String> selectCertiList = (List<String>) paramMap.get("trstCertiNoList");
		for(int i=0; i<selectCertiList.size(); i++) {
			Map<String, Object> selectTaxBilgDetailList = ar04Mapper.selectTaxBilgDetail(selectCertiList.get(i));
			
			if(MapUtils.getString(selectTaxBilgDetailList, "bilgCertNo") != null) {
				throw new Exception();
			}
			
		}
		
		// 비고1에 현장 put
		int siteCnt = Integer.parseInt(bilgInfo.get("siteCnt"));
		if (null != bilgInfo.get("siteNm")) {
			String ftxac11 = bilgInfo.get("siteNm").toString();
			if (siteCnt > 1) {
				// 한건이상일때만 " 외" 붙힐것.
				ftxac11 = ftxac11 + " 외";
			}
			bilgInfo.put("ftxac11", ftxac11);
		}

		// 비고2에 검색기간 put
		bilgInfo.put("ftxac21", "기간: " + paramMap.get("ftxac21").toString());

		// 비고3에 은행/계좌번호/예금주 put
		if (null != bilgInfo.get("bankNm") && null != bilgInfo.get("bkacNo") && null != bilgInfo.get("bkacOwner")) {
			bilgInfo.put("ftxac31",
					bilgInfo.get("bankNm") + "/" + bilgInfo.get("bkacNo") + "/" + bilgInfo.get("bkacOwner"));
		}

		ar04Mapper.insertBilg(bilgInfo);

		List<String> trstCertiNoList = (List<String>) paramMap.get("trstCertiNoList");
		Map<String, String> arParam = new HashMap<String, String>();
		arParam.put("trstCertiNo", trstCertiNoList.get(0));
		arParam = ar02Mapper.selectSellInfo(arParam);
		Map<String, String> bilgDetail = new HashMap<String, String>();
		String ard5006 = trstCertiNoList.size() > 1 ? arParam.get("prdtNm") + " 외 " + (trstCertiNoList.size() - 1) + "건"
				: arParam.get("prdtNm");
		bilgDetail.put("bilgCertNo", bilgCertNo);
		bilgDetail.put("moa95004", arParam.get("trstDt"));
		bilgDetail.put("ard5006", ard5006);
		bilgDetail.put("ard113a", arParam.get("trstPrdtCd"));
		bilgDetail.put("mea106154", arParam.get("prdtUnitNm"));
		bilgDetail.put("dms1056", arParam.get("prdtSpec"));
		// prdtLen 길이를 추가하기 위한 값
		
		double bilgQty = MapUtils.getDouble(bilgInfo, "bilgQty");
		bilgQty = Math.round(bilgQty*100)/100.0;
		String strBilgQty = Double.toString(bilgQty);
		
		bilgDetail.put("mea106314", strBilgQty);
		// bilgDetail.put("mea106314", bilgInfo.get("bilgQty"));
		
		bilgDetail.put("moa1023", bilgInfo.get("bilgAmt"));
		bilgDetail.put("moa10124", bilgInfo.get("taxMoa5124"));
		bilgDetail.put("dms1000", arParam.get("trspRmk"));
		bilgDetail.put("userId", paramMap.get("userId").toString());
		bilgDetail.put("pgmId", paramMap.get("pgmId").toString());
		ar04Mapper.insertBilgDetail(bilgDetail);

		// 청구번호 UPDATE
		for (String trstCertiNo : trstCertiNoList) {
			Map<String, String> sellParam = new HashMap<String, String>();
			sellParam.put("trstCertiNo", trstCertiNo);
			sellParam.put("bilgCertNo", bilgInfo.get("bilgCertNo"));
			ar02Mapper.updatePchsSellBilg(sellParam);
		}
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

		// 수신담당자 검색 후 없으면 insert (수신담당자 순번을 만들어야 하기 떄문에 ar04 update보다 먼저 수신담당자 검색 / 추가
		// 함)
		List<Map<String, String>> taxRcvList = ar06Mapper.selectTaxRcvInfo(paramMap);
		if (taxRcvList.size() < 1) {
			ar06Mapper.insertTaxRcvInfo(paramMap);
		} else {
			for (Map<String, String> taxRcvInfo : taxRcvList) {
				if (taxRcvInfo.get("useYn").equals("N")) {
					taxRcvInfo.put("USER_ID", paramMap.get("userId"));
					taxRcvInfo.put("PGM_ID", paramMap.get("pgmId"));
					ar06Mapper.updateTaxRcvInfoUseY(taxRcvInfo);
				}
				paramMap.put("taxRcvSn", taxRcvInfo.get("taxRcvSn")); // taxRcvSn 추가
			}
		}

		result = ar04Mapper.updateTaxBilg(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {
		}.getType();
		// 세금계산서관리 상세 delete
		ar04Mapper.deleteTaxBilgDetail(paramMap);
		// 세금계산서관리 상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("bilgCertNo", paramMap.get("bilgCertNo"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			if (!detailMap.get("ard113a").isEmpty()) {
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
		String loginId = null;
		String sndYn = String.valueOf(paramMap.get("sndYn"));
		if("Y".equals(sndYn)) {
			loginId = yamlReader.getKlnet().getLoginId();
		}else {
			loginId = yamlReader.getKlnet().getTestId();
		}
		
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		String selectDt = String.valueOf(paramMap.get("selectDt")); /* 세금계산서 발행일자 */
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		// List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		int msgId = 0;
		
		// for문 제거 start
		String xmlMsgId = "";
		Map<String, String> taxHdParam = new HashMap<String, String>();
		taxHdParam.put("msgId", Integer.toString(msgId));
		taxHdParam.put("sndYn", sndYn);
		taxHdParam.put("loginId", loginId);
		String bgm1004 = ""; // AR04 - taxBilgNo
		String orgnTaxBilgNo = "";
		taxHdParam.put("userId", userId);
		taxHdParam.put("userNm", userNm);
		taxHdParam.put("deptId", deptId);
		taxHdParam.put("pgmId", pgmId);
		taxHdParam.put("selectDt", selectDt); /* 세금계산서 발행일자 */
		taxHdParam.put("bilgCertNo", list.get(0).split(",")[0]);
		taxHdParam.put("coCd", list.get(0).split(",")[1]);
		Map<String, String> bilgInfo = new HashMap<String, String>();
		bilgInfo.put("taxBilgNo", "");
		bilgInfo.putAll(ar04Mapper.selectBilgInfo(taxHdParam));
		Map<String, String> temp = new HashMap<String, String>();
		orgnTaxBilgNo = bilgInfo.get("taxBilgNo");
		taxHdParam.put("orgnTaxBilgNo", "");
		taxHdParam.put("bgm1225", "9"); // 정발행은 9
		taxHdParam.put("invSndYn", bilgInfo.get("invSndYn"));
		if (bilgInfo.get("rffGn1").equals("RFFGN102")
				&& bilgInfo.get("rffAea").equals("RFFAEA01") /* && bilgInfo.get("taxBilgNo") != null */) {
			// 수정세금계산서 취소 로직 시작
			msgId++;
			bgm1004 = ar04Mapper.selectBgmSeq();
			xmlMsgId = ar04Mapper.selectMsgId(msgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "938"); // 세금계산서 938
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

			// 수정거래명세서 취소 로직 시작
			msgId++; // XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
			result = ar04Mapper.insertInvHdCancel(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertInvItemCancel(taxHdParam); // 거래명세서용 inv item insert

			// 연계문서 발행
			msgId++; // XML_MSG_ID 생성
			insertKladdi(msgId, taxHdParam);
		}

		if (bilgInfo.get("rffGn1").equals("RFFGN101")) {// 계산서 번호가 있고 수정 세금계산서가 아닌경우 기존 BGM1004 사용
			if (bilgInfo.get("taxBilgNo").isEmpty()) {
				bgm1004 = ar04Mapper.selectBgmSeq();
			} else {
				bgm1004 = bilgInfo.get("taxBilgNo");
			}
		} else if (!bilgInfo.get("rffGn1").equals("RFFGN101")) {// 계산서 번호가 없거나 수정 세금계산서인 경우 새로운 BGM1004 따야함
			bgm1004 = ar04Mapper.selectBgmSeq();
		}

		taxHdParam.put("rffGn1", bilgInfo.get("rffGn1"));
		taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
		taxHdParam.put("rffAea", bilgInfo.get("rffAea"));
		// 세금계산서 발행
		msgId++;
		xmlMsgId = ar04Mapper.selectMsgId(msgId);
		taxHdParam.put("bgm1004", bgm1004);
		taxHdParam.put("xmlMsgId", xmlMsgId);
		taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
		taxHdParam.put("docCode", "938"); // 세금계산서 938

		if (taxHdParam.get("invSndYn").equals("Y")) {
			taxHdParam.put("bgm1060", "COINTX");
		} else {
			taxHdParam.put("bgm1060", "");
		}
		result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
		result = ar04Mapper.insertTaxHd(taxHdParam); // taxHd insert
		ar04Mapper.updateTaxBilgNo(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트
		result = ar04Mapper.insertTaxDtl(taxHdParam);
		result = ar04Mapper.insertTaxItem(taxHdParam);

		// 거래명세서 발행
		if (taxHdParam.get("invSndYn").equals("Y")) {
			msgId++; // XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
			result = ar04Mapper.insertInvHd(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertItem(taxHdParam); // 거래명세서용 inv item insert

			// 연계문서 발행
			msgId++; // XML_MSG_ID 생성
			insertKladdi(msgId, taxHdParam);
		}
		// for문 제거 end
		
		return result;
	}

	// 수정세금계산서 발행
	@SuppressWarnings("all")
	@Override
	public int insertTaxHdUpdate(Map<String, Object> paramMap) {
		int result = 0;
		String loginId = null;
		String sndYn = String.valueOf(paramMap.get("sndYn"));
		if("Y".equals(sndYn)) {
			loginId = yamlReader.getKlnet().getLoginId();
		}else {
			loginId = yamlReader.getKlnet().getTestId();
		}
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		String rffAea = String.valueOf(paramMap.get("rffAea"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		// List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		String selectDt = String.valueOf(paramMap.get("selectDt")); /* 세금계산서 발행일자 */
		int msgId = 0;
		// for문 제거 start
		String xmlMsgId = "";
		Map<String, String> taxHdParam = new HashMap<String, String>();
		taxHdParam.put("msgId", Integer.toString(msgId));
		taxHdParam.put("sndYn", sndYn);
		taxHdParam.put("loginId", loginId);
		String bgm1004 = "";
		String orgnTaxBilgNo = "";
		taxHdParam.put("userId", userId);
		taxHdParam.put("userNm", userNm);
		taxHdParam.put("deptId", deptId);
		taxHdParam.put("pgmId", pgmId);
		taxHdParam.put("selectDt", selectDt); /* 세금계산서 발행일자 */
		taxHdParam.put("bilgCertNo", list.get(0).split(",")[0]);
		taxHdParam.put("coCd", list.get(0).split(",")[1]);
		Map<String, String> bilgInfo = new HashMap<String, String>();
		bilgInfo.put("taxBilgNo", "");
		bilgInfo.putAll(ar04Mapper.selectBilgInfo(taxHdParam));
		Map<String, String> temp = new HashMap<String, String>();
		orgnTaxBilgNo = bilgInfo.get("taxBilgNo");
		taxHdParam.put("orgnTaxBilgNo", "");
		taxHdParam.put("bgm1225", "9"); // 정발행은 9
		taxHdParam.put("invSndYn", bilgInfo.get("invSndYn"));
		// if (bilgInfo.get("rffGn1").equals("RFFGN101")
		// && bilgInfo.get("rffAea").isEmpty()) { // 일반세금계산서이고 수정사유가 없으면 (-) 수정세금계산서
		// 발행(기존 ar04 복사)
		// 수정세금계산서 취소 로직 시작
		msgId++;
		bgm1004 = ar04Mapper.selectBgmSeq();
		xmlMsgId = ar04Mapper.selectMsgId(msgId);
		taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
		taxHdParam.put("docCode", "938"); // 세금계산서 938
		taxHdParam.put("bgm1004", bgm1004);
		taxHdParam.put("xmlMsgId", xmlMsgId);
		taxHdParam.put("taxBilgNo", bilgInfo.get("taxBilgNo"));
		taxHdParam.put("rffGn1", "RFFGN102"); // 수정세금계산서
		taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
		taxHdParam.put("rffAea", bilgInfo.get("rffAea"));
		taxHdParam.put("orgnTaxBilgNo", orgnTaxBilgNo);
		taxHdParam.put("beforeBilgCertNo", taxHdParam.get("bilgCertNo"));
		String bilgCertNo = String.valueOf(ar04Mapper.getBilgCertNo());
		taxHdParam.put("bilgCertNo", bilgCertNo);
		taxHdParam.put("rffAea", rffAea);
		if (taxHdParam.get("invSndYn").equals("Y")) {
			taxHdParam.put("bgm1060", "COINTX");
		} else {
			taxHdParam.put("bgm1060", "");
		}
		ar04Mapper.insertCopyBilgTax(taxHdParam); // (-)세금계산서 추가 후 tax, inv, kladdi 발행
		ar04Mapper.updateOrgnTaxBilgNo(taxHdParam); // (-)세금계산서 추가 후 tax, inv, kladdi 발행
		ar04Mapper.insertCopyTaxBilgDetail(taxHdParam); // (-)세금계산서 추가 후 tax, inv, kladdi 발행
		// ar02업데이트
		// ar04d 업데이트

		result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
		ar04Mapper.insertTaxHd(taxHdParam); // tax bilg no 으로 bgm1004를 찾아서 금액을 -1 곱한 tax hd 를 insert
		ar04Mapper.insertTaxDtl(taxHdParam);
		result = ar04Mapper.insertTaxItem(taxHdParam);

		// 수정거래명세서 취소 로직 시작
		msgId++; // XML_MSG_ID 생성
		xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
		taxHdParam.put("xmlMsgId", xmlMsgId);
		taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
		taxHdParam.put("docCode", "780"); // 거래명세서 780
		result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
		result = ar04Mapper.insertInvHd(taxHdParam); // 거래명세서용 inv Hd insert
		result = ar04Mapper.insertInvDtlUpdate(taxHdParam); // 거래명세서용 inv dtl insert
		result = ar04Mapper.insertItemMinus(taxHdParam); // 거래명세서용 inv item insert
		// ar04Mapper.updateTaxBilgNo(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트

		// 연계문서 발행
		msgId++; // XML_MSG_ID 생성
		insertKladdi(msgId, taxHdParam);
		
		//AR02 --> 청구번호 제거, 원본세금계산서번호, 수정사유 Update
		if (taxHdParam.get("rffAea").equals("RFFAEA04")) { //수정사유가 RFFAEA04:계약해지일경우 AR02 원본계산서 번호, 수정사유 Update 안함
			taxHdParam.put("rffAea", "");
			taxHdParam.put("orgnTaxBilgNo", "");
		} 
		ar04Mapper.updateTrstInfo(taxHdParam); // AR02 Update
		
		// for문 제거 end
		return result;
	}

	@SuppressWarnings("all")
	@Override
	public int insertTaxHdReSend(Map<String, Object> paramMap) {
		int result = 0;
		String loginId = null;
		String sndYn = String.valueOf(paramMap.get("sndYn"));
		if("Y".equals(sndYn)) {
			loginId = yamlReader.getKlnet().getLoginId();
		}else {
			loginId = yamlReader.getKlnet().getTestId();
		}
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		String selectDt = String.valueOf(paramMap.get("selectDt"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		// List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		int msgId = 0;
		// for문 제거 start
		String xmlMsgId = "";
		Map<String, String> taxHdParam = new HashMap<String, String>();
		taxHdParam.put("msgId", Integer.toString(msgId));
		taxHdParam.put("sndYn", sndYn);
		taxHdParam.put("loginId", loginId);
		String bgm1004 = "";
		taxHdParam.put("userId", userId);
		taxHdParam.put("userNm", userNm);
		taxHdParam.put("deptId", deptId);
		taxHdParam.put("pgmId", pgmId);
		taxHdParam.put("bilgCertNo", list.get(0).split(",")[0]);
		taxHdParam.put("coCd", list.get(0).split(",")[1]);
		Map<String, String> bilgInfo = new HashMap<String, String>();
		bilgInfo.putAll(ar04Mapper.selectBilgInfo(taxHdParam));
		taxHdParam.put("invSndYn", bilgInfo.get("invSndYn"));

		bgm1004 = bilgInfo.get("taxBilgNo");

		msgId++;
		xmlMsgId = ar04Mapper.selectMsgId(msgId);
		taxHdParam.put("bgm1004", bgm1004);
		taxHdParam.put("xmlMsgId", xmlMsgId);
		taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
		taxHdParam.put("docCode", "938"); // 세금계산서 938
		taxHdParam.put("bgm1060", ""); // 거래명세서연계
		taxHdParam.put("selectDt", selectDt);
		if (taxHdParam.get("invSndYn").equals("Y")) {
			taxHdParam.put("bgm1060", "COINTX");
		} else {
			taxHdParam.put("bgm1060", "");
		}
		
		ar04Mapper.updateTaxHd(taxHdParam);
		
		result = ar04Mapper.insertTaxHd(taxHdParam); // taxHd insert
		result = ar04Mapper.insertTaxDtl(taxHdParam);
		result = ar04Mapper.insertTaxItem(taxHdParam);
		result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert

		// 거래명세서 발행
		if (taxHdParam.get("invSndYn").equals("Y")) {
			msgId++; // XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertInvHd(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertItem(taxHdParam); // 거래명세서용 inv item insert
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert

			// 연계문서 발행
			msgId++; // XML_MSG_ID 생성
			insertKladdi(msgId, taxHdParam);
		}
		// for문 제거 end
		return result;
	}

	@SuppressWarnings("all")
	@Override
	public int insertTaxHdCancel(Map<String, Object> paramMap) {
		int result = 0;
		String loginId = null;
		String sndYn = String.valueOf(paramMap.get("sndYn"));
		if("Y".equals(sndYn)) {
			loginId = yamlReader.getKlnet().getLoginId();
		}else {
			loginId = yamlReader.getKlnet().getTestId();
		}
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String> taxHdInfo = new HashMap<String, String>();
		int msgId = 0;
		// for문 제거 start
		String xmlMsgId = "";
		Map<String, String> taxHdParam = new HashMap<String, String>();
		taxHdParam.put("msgId", Integer.toString(msgId));
		taxHdParam.put("sndYn", sndYn);
		taxHdParam.put("loginId", loginId);
		String bgm1004 = "";

		taxHdParam.put("userId", userId);
		taxHdParam.put("userNm", userNm);
		taxHdParam.put("deptId", deptId);
		taxHdParam.put("pgmId", pgmId);
		taxHdParam.put("bilgCertNo", list.get(0).split(",")[0]);
		taxHdParam.put("coCd", list.get(0).split(",")[1]);
		Map<String, String> bilgInfo = ar04Mapper.selectBilgInfo(taxHdParam);

		if (bilgInfo.get("taxAdmsYn").equals("Y")) { // 계산서 승인 여부 Y -> 계산서 환입 처리 및 tax, inv 테이블 insert
			// 수정세금계산서 취소 로직 시작
			msgId++;

			bgm1004 = ar04Mapper.selectBgmSeq();
			xmlMsgId = ar04Mapper.selectMsgId(msgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "938"); // 세금계산서 938
			taxHdParam.put("bgm1004", bgm1004);
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("taxBilgNo", bilgInfo.get("taxBilgNo"));
			taxHdParam.put("rffGn1", "RFFGN102");
			taxHdParam.put("rffGn2", bilgInfo.get("rffGn2"));
			taxHdParam.put("rffAea", "RFFAEA03");
			if (taxHdParam.get("invSndYn").equals("Y")) {
				taxHdParam.put("bgm1060", "COINTX");
			} else {
				taxHdParam.put("bgm1060", "");
			}
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 세금계산서용 mapoutkey insert
			ar04Mapper.insertTaxHdCancel(taxHdParam); // tax bilg no 으로 bgm1004를 찾아서 금액을 -1 곱한 tax hd 를 insert
			ar04Mapper.insertTaxDtl(taxHdParam);
			result = ar04Mapper.insertTaxItemCancel(taxHdParam);
			ar04Mapper.updateTaxBilgNoCancel(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서,
															// 수정사유코드 : 환입

			// 수정거래명세서 취소 로직 시작
			msgId++; // XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
			result = ar04Mapper.insertInvHdCancel(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertInvItemCancel(taxHdParam); // 거래명세서용 inv item insert

			// 연계문서 발행
			msgId++; // XML_MSG_ID 생성
			insertKladdi(msgId, taxHdParam);
		} else { // 계산서 승인 전 -> bgm1225 를 3으로 계산서 발행
			// 수정세금계산서 "삭제" 로직 시작
			msgId++;

			// bgm1004 = ar04Mapper.selectBgmSeq();
			xmlMsgId = ar04Mapper.selectMsgId(msgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "938"); // 세금계산서 938
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
			ar04Mapper.updateTaxBilgNoDelete(taxHdParam); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서,
															// 수정사유코드 : 환입

			// 수정거래명세서 취소 로직 시작
			msgId++; // XML_MSG_ID 생성
			xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
			taxHdParam.put("xmlMsgId", xmlMsgId);
			taxHdParam.put("docName", "VATDEC"); // 세금계산서 VATDEC
			taxHdParam.put("docCode", "780"); // 거래명세서 780
			result = ar04Mapper.insertMapoutKey(taxHdParam); // 거래명세서용 mapoutkey insert
			result = ar04Mapper.insertInvHdDelete(taxHdParam); // 거래명세서용 inv Hd insert
			result = ar04Mapper.insertInvDtl(taxHdParam); // 거래명세서용 inv dtl insert
			result = ar04Mapper.insertInvItemDelete(taxHdParam); // 거래명세서용 inv item insert

			// 연계문서 발행
			msgId++; // XML_MSG_ID 생성
			insertKladdi(msgId, taxHdParam);
			ar04Mapper.updateTrstInfo(taxHdParam); // (-)세금계산서 추가 후 tax, inv, kladdi 발행
		}
		// for문 제거 end
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
		
		// 수정세금계산서 발행 때문에 taxBilgNo는 없지만 orgnTaxBilgNo가 있는 케이스가 발생하므로 taxBilgNo만 없으면 확정취소가 되도록 변경함. - 20210721 김대연
		// if (bilgInfo.get("taxBilgNo") == null && bilgInfo.get("orgnTaxBilgNo") == null) {
		if (bilgInfo.get("taxBilgNo") == null) {
			result = ar04Mapper.deleteBilgInfo(param);
			ar02Mapper.updateBilgCancel(param);
//			ar05Mapper.updateTrstCertiNo(param); // 매입과 매칭된것 제거 //확정취소를 할때 매핑취소가 되지 않게함. 그 전에 이미 매핑관련 처리를 하기 때문 - 20210930 권승경
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
		String selectDt = String.valueOf(paramMap.get("selectDt")); /* 세금계산서 발행일자 */
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		// List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, String> param = new HashMap<String, String>();
		int msgId = 0;
		// for문 제거 start
		param.put("userId", userId);
		param.put("userNm", userNm);
		param.put("deptId", deptId);
		param.put("pgmId", pgmId);
		param.put("selectDt", selectDt); /* 세금계산서 발행일자 */
		param.put("bilgCertNo", list.get(0).split(",")[0]);
		param.put("coCd", list.get(0).split(",")[1]);
		result = ar04Mapper.updateBilgRvrs(param); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서, 수정사유코드 :
		// for문 제거 end
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public int updateBilgRvrsCancel(Map<String, Object> paramMap) {

		int result = 0;
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String deptId = String.valueOf(paramMap.get("deptId"));
		List<String> list = (List<String>) paramMap.get("bilgCertNoArr");
		// List<String> cdCdList = (List<String>) paramMap.get("coCdArr");
		Map<String, String> param = new HashMap<String, String>();
		int msgId = 0;
		// for문 제거 start
		param.put("userId", userId);
		param.put("userNm", userNm);
		param.put("deptId", deptId);
		param.put("pgmId", pgmId);
		param.put("bilgCertNo", list.get(0).split(",")[0]);
		param.put("coCd", list.get(0).split(",")[1]);
		result = ar04Mapper.updateBilgRvrsCancel(param); // taxHd에 BGM_1004를 ar04테이블에 업데이트, 세금계산서종류 : 수정 세금계산서,
		// for문 제거 end
		return result;
	}

	@Override
	public int updateBilg(Map<String, Object> paramMap) {
		int result = 0;
		String bilgCertNo = String.valueOf(paramMap.get("bilgCertNo"));
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", String.valueOf(paramMap.get("userId")));
		param.put("userNm", String.valueOf(paramMap.get("userNm")));
		param.put("bilgCertNo", bilgCertNo);
		Map<String, String> bilgInfo = ar04Mapper.selectTaxBilg(param);
		if (bilgInfo.get("taxBilgNo") != null)
			return result;
		Map<String, String> bilgParam = ar02Mapper.selectBilgInfoUpdate(param);
		result = ar04Mapper.updateBilgAmt(bilgParam);
		return result;
	}

	int insertKladdi(int msgId, Map<String, String> param) {
		int result = 0;
		
		// 연계문서 발행
		String xmlMsgId = ar04Mapper.selectMsgId(msgId);// 새 메시지아이디 생성
		param.put("xmlMsgId", xmlMsgId);
		param.put("docName", "KLADDI"); // 연계문서 KLADDI
		param.put("docCode", "6KQ"); // 문서코드 6KQ
		result = ar04Mapper.insertMapoutKey(param); // 연계문서 mapoutkey insert
		result = ar04Mapper.insertKladdiHd(param); // 연계문서 kladdi Hd insert
		result = ar04Mapper.insertKladdiDtl(param); // 연계문서 kladdi dtl insert

		return result;
	}
	
	
	

	@Override
	@SuppressWarnings("all")
	public int updateNote(Map<String, Object> paramMap) {
		int result = 0;
		List<Map<String, Object>> rowlist = (List<Map<String, Object>>) paramMap.get("rows");
		
		Map<String, String> param = new HashMap<String, String>();
		
		for(int i=0; i<rowlist.size(); i++) {
			param.put("trstCertiNo", MapUtils.getString(rowlist.get(i), "trstCertiNo"));
			param.put("trspRmk", MapUtils.getString(rowlist.get(i), "trspRmk", ""));
			param.put("ordrgSeq", MapUtils.getString(rowlist.get(i), "ordrgSeq"));
			
			result = ar04Mapper.updateNote(param);
			
			if(paramMap.get("type").equals("OD")) {
				if(rowlist.get(i).containsKey("ordrgSeq")) {
					result = ar04Mapper.updateNoteData(param);
				}
			} else {
				if(rowlist.get(i).containsKey("ordrgSeq")) {
					result = ar04Mapper.updateNoteData2(param);
				}
			}
			
			
		}
		
		return result;
	}
	
	@SuppressWarnings("all")
	@Override
	public void deleteTaxHd(Map<String, Object> paramMap) {
		ar04Mapper.updateAR02BilgCertNo(paramMap);
		ar04Mapper.deleteTaxHd(paramMap);
	}
	
}