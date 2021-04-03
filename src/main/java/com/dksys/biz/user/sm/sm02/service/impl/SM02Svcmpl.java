package com.dksys.biz.user.sm.sm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sm.sm02.mapper.SM02Mapper;
import com.dksys.biz.user.sm.sm02.service.SM02Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
public class SM02Svcmpl implements SM02Svc {
	
    @Autowired
    SM02Mapper sm02Mapper;

    
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
	public void sm01UpdateInsertStockMove(Map<String, String> param) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type map = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> detail = gson.fromJson(param.get("detailArr2"), map);
		List<Map<String, String>> detailList = gson.fromJson(param.get("detailArr"), mapList);
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId",   param.get("userId").toString());
			detailMap.put("pgmId",    param.get("pgmId").toString());
			detailMap.put("sWhCd",    detail.get("sWhCd"));
			detailMap.put("sTransDt", detail.get("sTransDt"));
			detailMap.put("sRmk",     detail.get("sRmk"));
			detailMap.put("sellType", detail.get("sellType"));
	//		detailMap.put("sPrdtCd",  detail.get("sPrdtCd"));
			
			sm02Mapper.sm01UpdateInsertStockMove(detailMap);
			sm02Mapper.sm01UpdateStockMove(detailMap);
			sm02Mapper.sm02InsertStockMove(detailMap);
		}
		
	}

	@Override
	public void sm01UpdateInsertBarterStockMove(Map<String, String> param) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type map = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> detail = gson.fromJson(param.get("detailArr2"), map);
		List<Map<String, String>> detailList = gson.fromJson(param.get("detailArr"), mapList);
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId",    param.get("userId").toString());
			detailMap.put("pgmId",     param.get("pgmId").toString());
			detailMap.put("sWhCd",     detail.get("sWhCd"));
			detailMap.put("sTransDt",  detail.get("sTransDt"));
			detailMap.put("sRmk",      detail.get("sRmk"));
			detailMap.put("sellType",  detail.get("sellType"));
			detailMap.put("sPrdtCd",   detail.get("sPrdtCd"));
			detailMap.put("sPrdtSize", detail.get("sPrdtSize"));
			detailMap.put("sPrdtSpec", detail.get("sPrdtSpec"));
			detailMap.put("sPrdtLen",  detail.get("sPrdtLen"));
			
			sm02Mapper.sm01UpdateInsertBarterStockMove(detailMap);
			sm02Mapper.sm01UpdateStockMove(detailMap);
			sm02Mapper.sm02InsertBarterStockMove(detailMap);
		}
		
	}
	
}