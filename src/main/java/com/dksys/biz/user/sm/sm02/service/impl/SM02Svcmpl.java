package com.dksys.biz.user.sm.sm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ifsys.mes.mapper.MESSTOCKMapper;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
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
    SM01Mapper sm01Mapper;

	@Autowired
	SD07Mapper sd07Mapper;
	
	@Autowired
	MESSTOCKMapper messtockMapper;
    
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
	public int selectStockTernKeykMovePchListCount(Map<String, String> param) {
		return sm02Mapper.selectStockTernKeykMovePchListCount(param);
	}
	
	@Override
	public List<Map<String, String>> selectStockTernKeykMovePchList(Map<String, String> param) {
		return sm02Mapper.selectStockTernKeykMovePchList(param);
	}
	
	@Override
	public List<Map<String, String>> selectStockMoveStatMngmDtlList(Map<String, String> param) {
		return sm02Mapper.selectStockMoveStatMngmDtlList(param);
	}
	
	@Override
	public int sm02UpdateTernKeyStockMst(Map<String, String> param) {
		return sm02Mapper.sm02UpdateTernKeyStockMst(param);
	}
	
	@Override
	public List<Map<String, String>> sm02selectTernKeyStock(Map<String, String> param) {
		return sm02Mapper.sm02selectTernKeyStock(param);
	}
	
	@Override
	public int updateStockMoveRmk(Map<String, String> param) {
		return sm02Mapper.updateStockMoveRmk(param);
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
			detailMap.put("sellType",  detail.get("typCd"));
			detailMap.put("sPrjctCd",  detail.get("prjctCd"));   // 현재는 변동없음. 추하 변동시 바꿀것.
			detailMap.put("sPrdtCd",   detail.get("prdtCd"));
			detailMap.put("sPrdtSize", detail.get("prdtSize"));
			detailMap.put("sPrdtSpec", detail.get("prdtSpec"));
			detailMap.put("sPrdtLen",  detail.get("prdtLen"));
			detailMap.put("transSeq",  detail.get("transSeq"));
			detailMap.put("transAmt",  detail.get("transAmt"));
			
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
	public int insertUpdateTernKeyStockMove(Map<String, String> param) {
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
			 detailMap.put("prdtCd",    detailMap.get("trstPrdtCd").toString());
			 detailMap.put("sPrdtCd",   detailMap.get("trstPrdtCd").toString());
			 detailMap.put("sPrdtSpec", detailMap.get("prdtSpec").toString());
			 detailMap.put("sPrdtSize", detailMap.get("prdtSize").toString());
			 detailMap.put("sPrdtLen",  detailMap.get("prdtLen").toString());
			 detailMap.put("sCoCd",     detail.get("sCoCd"));
			 detailMap.put("sWhCd",     detail.get("sWhCd"));
			 detailMap.put("sTransDt",  detail.get("sTransDt"));
			 detailMap.put("sRmk",      detail.get("sRmk"));
			 detailMap.put("sellType",  detail.get("sellType"));
			 detailMap.put("sPrjctCd",  detail.get("sPrjctCd"));
			 detailMap.put("prdtCd",    detailMap.get("trstPrdtCd").toString());
			 detailMap.put("clntCd",    detailMap.get("trstClntCd").toString());
			 
			 /*
			 * 제강사 턴키 재고이동 순서
			 * 1) 상세정보를 재고장에 조회 후 가공 레코드가 있으면 UPDATE 가공 중량, 수량 -, 없으면 INSERT 가공 중량, 수량- 
			 * 2) 상세정보로 재고장에 조회 후 유통 레코드가 있으면 UPDATE 유통 중량, 수량 +, 없으면 INSERT 유통 중량, 수량 + 
			 * 3) 유통이동 이력 추가
			 * 4) MES 재고 추가
			 * */
			
			// 매입정보로 재고 마스터 조회 
			Map<String, String> selectedStock = sm01Mapper.selectStockInfo(detailMap);
			detailMap.put("stokSeq",   selectedStock.get("stokSeq"));
			detailMap.put("stockQty",  selectedStock.get("stockQty"));
			detailMap.put("stockWt",   selectedStock.get("stockWt"));
			detailMap.put("stockUpr",  selectedStock.get("stockUpr"));
			detailMap.put("stdUpr",    selectedStock.get("stdUpr"));
			detailMap.put("pchsUpr",   selectedStock.get("pchsUpr"));
			detailMap.put("sellUpr",   selectedStock.get("sellUpr"));
			
			/* AR02 매입 정보일 시 [TUNKEY_MOVE_YN] Y로 업데이트 */
			if(detailMap.get("trstCertiNo") != null) {
				sm02Mapper.sm03UpdateTernKeyYN(detailMap);
			}
			/* SM03 이동 정보일 시 [TUNKEY_MOVE_YN] Y로 업데이트 */
			if(detailMap.get("trstNo") != null){				
				sm02Mapper.updateTernKeyYN(detailMap);
			}		

			sm02Mapper.sm01UpdateStockMove(detailMap);        // 기존 차감.
			
		    sm02Mapper.sm02UpdateTernKeyStockMst(detailMap);  // 신규 + 추가
		    sm02Mapper.sm02InsertBarterStockMove(detailMap);  // 재고이동 이력
		    
//			/* 3) 유통이동 이력 추가 */
//			sm02Mapper.sm03InsertStockMove(detailMap);
//			/* 4) MES 재고 이동 정보 추가 */

//		    messtockMapper.insertIfMesStockMove(detailMap); 
			insertIfMesStockMove(detailMap); 
		    
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
	
	@Override
	public int insertIfMesStockMove(Map<String, String> detailMap) {

		//	    WH01	진천공장  J
        //	    WH05	인천공장  N
        //	    WH06	창녕공장  C
	    if      ("WH01".equals(detailMap.get("sWhCd"))) { 	detailMap.put("worksCd",   "J");
	    }else if("WH05".equals(detailMap.get("sWhCd"))) {  detailMap.put("worksCd",   "N");
	    }else {                                         detailMap.put("worksCd",   "C");
	    }
	    if( "C".equals(detailMap.get("prdtCoilYn"))) {
			detailMap.put("productNameCd",   "BC"); /* 코일철근 : BC */
	    }else {
			detailMap.put("productNameCd",   "BD"); /* 바철근 : BD */
	    }
		detailMap.put("moveSiteCd",   "R"); /* 유통으로이동 : R */

	    messtockMapper.insertIfMesStockMove(detailMap); 
		return 200;
	}
}