package com.dksys.biz.user.sm.sm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sm.sm02.mapper.SM02Mapper;
import com.dksys.biz.user.sm.sm02.service.SM02Svc;
import com.dksys.biz.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional("erpTransactionManager")
public class SM02Svcmpl implements SM02Svc {
	
    @Autowired
    SM02Mapper sm02Mapper;

	@Autowired
	SD07Mapper sd07Mapper;
    
	@Override
	public List<Map<String, String>> selectCmnCodeList(Map<String, String> param) {
		return sm02Mapper.selectCmnCodeList(param);
	}
	
	@Override
	public List<Map<String, String>> selectDtlCmnCodeList(Map<String, String> param) {
		return sm02Mapper.selectDtlCmnCodeList(param);
	}

	@Override
	public List<Map<String, String>> selectClntSearchList(Map<String, String> param) {
		return sm02Mapper.selectClntSearchList(param);
	}

	@Override
	public List<Map<String, String>> selectPrdtSearchList(Map<String, String> param) {
		return sm02Mapper.selectPrdtSearchList(param);
	}


	@Override
	public int selectUprCount(Map<String, String> param) {
		return sm02Mapper.selectUprCount(param);
	}


	@Override
	public List<Map<String, String>> selectStockMoveStatMngmList(Map<String, String> param) {
		return sm02Mapper.selectStockMoveStatMngmList(param);
	}
	
	@Override
	public int selectUprDtlCount(Map<String, String> param) {
		return sm02Mapper.selectUprDtlCount(param);
	}
	
	@Override
	public List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param) {
		return sm02Mapper.selectStockMoveStatMngmDtlList(param);
	}
	
	/*
	 * @Override public int sm01CheckCnt(Map<String, Object> param) {
	 * List<Map<String, String>> detailList = (List<Map<String, String>>)
	 * param.get("detailArr"); for (Map<String, String> detailMap : detailList) {
	 * detailMap.put("coCd", param.get("coCd").toString()); detailMap.put("mWhCd",
	 * param.get("whCd").toString()); detailMap.put("mTypCd",
	 * param.get("whCd").toString()); detailMap.put("mMakrCd",
	 * param.get("whCd").toString()); detailMap.put("mOwnerCd",
	 * param.get("whCd").toString()); detailMap.put("mPrdtCd",
	 * param.get("whCd").toString()); detailMap.put("mImpYn",
	 * param.get("whCd").toString()); result +=
	 * ar02Mapper.updatePchsSell(detailMap); } return
	 * sm02Mapper.sm01CheckCnt(param); }
	 */

	/*
	 * @Override public int sm01InsertStockMove(Map<String, Object> param) { return
	 * sm02Mapper.sm01InsertStockMove(param); }
	 */

	@Override
	public int sm01UpdateInsertStockMove(Map<String, String> param) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type map = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> detail = gson.fromJson(param.get("detailArr2"), map);
		//마감 체크 .. 매출
		param.put("coCd",  detail.get("sCoCd"));
		param.put("sTransDt",  detail.get("sTransDt"));
		if(checkStockClose(param)) {
			return 500;
		}			
		List<Map<String, String>> detailList = gson.fromJson(param.get("detailArr"), mapList);
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId",    param.get("userId").toString());
			detailMap.put("pgmId",     param.get("pgmId").toString());
			detailMap.put("sCoCd",     detail.get("sCoCd"));
			detailMap.put("sWhCd",     detail.get("sWhCd"));
			detailMap.put("sTransDt",  detail.get("sTransDt"));
			detailMap.put("sRmk",      detail.get("sRmk"));
			detailMap.put("sellType",  detail.get("sellType"));
			detailMap.put("sPrjctCd",  detail.get("sPrjctCd"));
			detailMap.put("sPrdtCd",   detailMap.get("prdtCd"));
			detailMap.put("sPrdtSpec", detailMap.get("prdtSpec"));
			detailMap.put("sPrdtSize", detailMap.get("prdtSize"));
			detailMap.put("sPrdtLen",  detailMap.get("prdtLen"));
			detailMap.put("transSeq",  detail.get("transSeq"));
			detailMap.put("transAmt",  detail.get("transAmt"));

			sm02Mapper.sm01UpdateStockMove(detailMap);       // 기존 차감.
			
			//  가공으로 이동 시에는 길이가 8M가 된다 -- 공장에서 길이 관리가 안됨
			if("8".equals(param.get("mngPrdtLen"))) {
				detailMap.put("sPrdtLen",  param.get("mngPrdtLen"));				
			}else {
				detailMap.put("sPrdtLen",  detailMap.get("prdtLen"));
			}
			sm02Mapper.sm01UpdateInsertStockMove(detailMap); // 신규 추가			
			sm02Mapper.sm02InsertStockMove(detailMap);       // 재고이동 이력
		}
		return 200;
		
	}

	@Override
	public int sm01UpdateInsertBarterStockMove(Map<String, String> param) {

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type map = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> detail = gson.fromJson(param.get("detailArr2"), map);	

		//마감 체크 .. 매출
		param.put("coCd",  detail.get("sCoCd"));
		param.put("sTransDt",  detail.get("sTransDt"));
		if(checkStockClose(param)) {
			return 500;
		}	
		
		List<Map<String, String>> detailList = gson.fromJson(param.get("detailArr"), mapList);
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId",    param.get("userId").toString());
			detailMap.put("pgmId",     param.get("pgmId").toString());
			detailMap.put("sCoCd",     detail.get("sCoCd"));
			detailMap.put("sWhCd",     detail.get("sWhCd"));
			detailMap.put("sTransDt",  detail.get("sTransDt"));
			detailMap.put("sRmk",      detail.get("sRmk"));
			detailMap.put("sellType",  detail.get("sellType"));
			detailMap.put("sPrjctCd",  detail.get("sPrjctCd"));   // 현재는 변동없음. 추하 변동시 바꿀것.
			detailMap.put("sPrdtCd",   detail.get("sPrdtCd"));
			detailMap.put("sPrdtSize", detail.get("sPrdtSize"));
			detailMap.put("sPrdtSpec", detail.get("sPrdtSpec"));
			detailMap.put("sPrdtLen",  detail.get("sPrdtLen"));
			
			sm02Mapper.sm01UpdateStockMove(detailMap);      // 기존 차감.
			
			//  가공으로 이동 시에는 길이가 8M가 된다 -- 공장에서 길이 관리가 안됨
			if("8".equals(param.get("mngPrdtLen"))) {
				detailMap.put("sPrdtLen",  param.get("mngPrdtLen"));				
			}			
			sm02Mapper.sm01UpdateInsertBarterStockMove(detailMap);  // 신규 추가
			sm02Mapper.sm02InsertBarterStockMove(detailMap);        // 재고이동 이력
		}
		return 200;
		
	}
	
	@Override
	public boolean checkStockClose(Map<String, String> paramMap) {
		String trstDt = paramMap.get("sTransDt").replace("-", "");
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		if(sd07result != null) {
			int today = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay = Integer.parseInt(sd07result.get("stockCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("stockCloseYn")) && today > closeDay) {
				return true;
			}
		}
		return false;
	}
	
}