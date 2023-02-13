package com.dksys.biz.user.sm.sm02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ifsys.mes.mapper.MESSTOCKMapper;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sd.sd09.mapper.SD09Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.user.sm.sm02.mapper.SM02Mapper;
import com.dksys.biz.user.sm.sm02.service.SM02Svc;
import com.dksys.biz.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class SM02Svcmpl implements SM02Svc {

    @Autowired
    SM02Mapper sm02Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;

	@Autowired
	SD07Mapper sd07Mapper;

	@Autowired
	SD09Mapper sd09Mapper;
	
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
	
	@Override
	public int updateStockMoveCaryng(Map<String, String> param) {
		return sm02Mapper.updateStockMoveCaryng(param);
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
			
			// 재고이동시 기존 제품의 데이터가 없을 때 키,값이 들어오지 않기 때문에 강제로 넣어주는 것
			List<String> containList = new ArrayList<>();
			containList.add("prjctCd");
			containList.add("prdtCd");
			containList.add("prdtSize");
			containList.add("prdtSpec");
			containList.add("prdtLen");
			containList.add("clntCd");
			
			for(int i=0; i<containList.size(); i++) {
				if(!detailMap.containsKey(containList.get(i))) {
					detailMap.put(containList.get(i), "0");
				}
			}
			
			detailMap.put("userId",       param.get("userId").toString());
			detailMap.put("pgmId",        param.get("pgmId").toString());
			detailMap.put("sCoCd",        detail.get("sCoCd"));
			detailMap.put("sWhCd",        detail.get("sWhCd"));
			detailMap.put("sTransDt",     detail.get("sTransDt"));
			detailMap.put("sRmk",         detail.get("sRmk"));
			detailMap.put("sellType",     detail.get("sellType"));
			detailMap.put("sPrjctCd",     detail.get("sPrjctCd"));
			detailMap.put("sPrdtCd",      detailMap.get("prdtCd"));
			detailMap.put("sPrdtSpec",    detailMap.get("prdtSpec"));
			detailMap.put("sPrdtSize",    detailMap.get("prdtSize"));
			detailMap.put("sPrdtLen",     detailMap.get("prdtLen"));
			detailMap.put("transSeq",     param.get("transSeq"));
			detailMap.put("transAmt",     param.get("transAmt"));
			detailMap.put("realStockYn",  detail.get("realStockYn"));
			detailMap.put("errCorecYn",   detail.get("errCorecYn"));
			//detailMap.put("impYn",		  detail.get("impYn"));
			//detailMap.put("estcoprtCd",   detail.get("estcoprtCd"));
			
			sm02Mapper.sm01UpdateStockMove(detailMap);       // 기존 차감.
			
			//  가공으로 이동 시에는 길이가 8M가 된다 -- 공장에서 길이 관리가 안됨
			if("8".equals(param.get("mngPrdtLen"))) {  
				detailMap.put("sPrdtLen",  param.get("mngPrdtLen"));				
			}else {
				/* 길이 변경 시
				 * 1) 길이변경 설정 시 기존차감 진행 시 기존길이로 재고차감 
				 * 2) 신규 추가 및 재고이동 이력은 변경된 길이 적용
				 */
				String prdtLenEdit = detailMap.get("prdtLenEdit");
				if("".equals(prdtLenEdit) || prdtLenEdit == null) { //길이 변경 chk
					detailMap.put("sPrdtLen",  detailMap.get("prdtLen"));
				}else {
					detailMap.put("sPrdtLen",  prdtLenEdit); 
				}
			}
			sm02Mapper.sm01UpdateInsertStockMove(detailMap); // 신규 추가		
			// 일반재고이동 운반비 등록 시 transSeq, transAmt set
			if(!("".equals(param.get("transSeq")) || param.get("transSeq") != null)) {
				detailMap.put("transAmt",  param.get("transAmt"));
				detailMap.put("transSeq",  param.get("transSeq"));
			}
			
			sm02Mapper.sm02InsertStockMove(detailMap);       // 재고이동 이력
			insertIfMesStockMove(detailMap);                 // MES If 입력
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
			
			// 재고이동시 기존 제품의 데이터가 없을 때 키,값이 들어오지 않기 때문에 강제로 넣어주는 것
			List<String> containList = new ArrayList<>();
			containList.add("prjctCd");
			containList.add("prdtCd");
			containList.add("prdtSize");
			containList.add("prdtSpec");
			containList.add("prdtLen");
			
			for(int i=0; i<containList.size(); i++) {
				if(!detailMap.containsKey(containList.get(i))) {
					detailMap.put(containList.get(i), "");
				}
			}
			
			detailMap.put("userId",       param.get("userId").toString());
			detailMap.put("pgmId",        param.get("pgmId").toString());
			detailMap.put("sCoCd",        detail.get("sCoCd"));
			detailMap.put("sWhCd",        detail.get("sWhCd"));
			detailMap.put("sTransDt",     detail.get("sTransDt"));
			detailMap.put("sRmk",         detail.get("sRmk"));
			detailMap.put("sellType",     detail.get("sellType"));
			detailMap.put("sPrjctCd",     detail.get("sPrjctCd"));   // 현재는 변동없음. 추하 변동시 바꿀것.
			detailMap.put("sPrdtCd",      detail.get("sPrdtCd"));
			detailMap.put("sPrdtSize",    detail.get("sPrdtSize"));
			detailMap.put("sPrdtSpec",    detail.get("sPrdtSpec"));
			detailMap.put("sPrdtLen",     detail.get("sPrdtLen"));
			detailMap.put("transSeq",     detail.get("transSeq"));
			detailMap.put("transAmt",     detail.get("transAmt"));
//			detailMap.put("realStockYn",  detail.get("realStockYn"));
			detailMap.put("errCorecYn",   detail.get("errCorecYn"));
			
			sm02Mapper.sm01UpdateStockMove(detailMap);      // 기존 차감.
			
			//  가공으로 이동 시에는 길이가 8M가 된다 -- 공장에서 길이 관리가 안됨
			if("8".equals(param.get("mngPrdtLen"))) {
				detailMap.put("sPrdtLen",  param.get("mngPrdtLen"));				
			}			
			sm02Mapper.sm01UpdateInsertBarterStockMove(detailMap);  // 신규 추가
			sm02Mapper.sm02InsertBarterStockMove(detailMap);        // 재고이동 이력
			// 	insertIfMesStockMove(detailMap);                    // MES If 입력   바터재고는 공장간 이동이 없으며 MES에서는 이미 생산하고 차감이 되었음으로 전송 필요 없음.
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
			 detailMap.put("userId",       param.get("userId").toString());
			 detailMap.put("pgmId",        param.get("pgmId").toString());
			 detailMap.put("prdtCd",       detailMap.get("trstPrdtCd").toString());
			 detailMap.put("sPrdtCd",      detailMap.get("trstPrdtCd").toString());
			 detailMap.put("sPrdtSpec",    detailMap.get("prdtSpec").toString());
			 detailMap.put("sPrdtSize",    detailMap.get("prdtSize").toString());
			 detailMap.put("sPrdtLen",     detailMap.get("prdtLen").toString());
			 detailMap.put("sCoCd",        detail.get("sCoCd"));
			 detailMap.put("sWhCd",        detail.get("sWhCd"));
			 detailMap.put("sTransDt",     detail.get("sTransDt"));
			 detailMap.put("sRmk",         detail.get("sRmk"));
			 detailMap.put("sellType",     detail.get("sellType"));
			 detailMap.put("sPrjctCd",     detail.get("sPrjctCd"));
			 detailMap.put("prdtCd",       detailMap.get("trstPrdtCd").toString());
			 detailMap.put("clntCd",       detailMap.get("trstClntCd").toString());
			 detailMap.put("realStockYn",  detail.get("realStockYn"));
			 detailMap.put("errCorecYn",   detail.get("errCorecYn"));
			 
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
				//매입정보 일 시 
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
//				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {					
//					paramMap.put("clntCd",  paramMap.get("whClntCd"));		
//				}
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
		
//		WH06	창녕공장	    WH	GGS	WHDIV01	ESTCOPRT5
//		WH13	지지스틸창녕공장	WH	GUM	WHDIV03	ESTCOPRT1
//		WH29	지지엠창녕	    WH	GGM	WHDIV02	ESTCOPRT4
//		WH32	지지스틸창녕공장	WH	GGM	WHDIV03	ESTCOPRT4
		
//		WH05	인천공장	    WH	GUM	WHDIV01	ESTCOPRT1
//		WH20	금문인천공장	WH	GGM	WHDIV03	ESTCOPRT4
		
//		WH01	진천공장	    WH	GUM	WHDIV01	ESTCOPRT1
//		WH19	금문진천공장	WH	GGM	WHDIV03	ESTCOPRT4
//		WH41	진천공장2	    WH	GUM	WHDIV01	ESTCOPRT1

		String mesIfDesYn = "Y";  // 도착지가 공장
		String mesIfStrYn = "Y";  // 출발지가 공장

		// 외부에서 공장으로 이동시에는 FROM 관련 자료는 없음

		detailMap.put("worksCdTo",      "");
		detailMap.put("worksCdFrom",   "");
		//도착공장
	    if      ("WH01".equals(detailMap.get("sWhCd")) || "WH19".equals(detailMap.get("sWhCd"))|| "WH41".equals(detailMap.get("sWhCd")) ) {  detailMap.put("worksCdTo",   "J");
	    }else if("WH05".equals(detailMap.get("sWhCd")) || "WH20".equals(detailMap.get("sWhCd")))                                          {  detailMap.put("worksCdTo",   "N");
	    }else if("WH06".equals(detailMap.get("sWhCd")) || "WH13".equals(detailMap.get("sWhCd")) || "WH29".equals(detailMap.get("sWhCd")) || "WH32".equals(detailMap.get("sWhCd"))) {       
	    	   detailMap.put("worksCdTo",   "C");	      
	    }else {
	         //  도착지 MES 전송 대상이 아님.
	    	 mesIfDesYn = "N";
	    }
	    //출발공장
	    if      ("WH01".equals(detailMap.get("whCd")) || "WH19".equals(detailMap.get("whCd"))|| "WH41".equals(detailMap.get("whCd"))) {
	                                                      detailMap.put("worksCd",   "J"); 
	                                                      detailMap.put("worksCdFrom",   "J");
	    }else if("WH05".equals(detailMap.get("whCd")) || "WH20".equals(detailMap.get("whCd")) ) {
	    	                                              detailMap.put("worksCd",   "N"); 
	    	                                              detailMap.put("worksCdFrom",   "N");
	    }else if("WH06".equals(detailMap.get("whCd")) || "WH13".equals(detailMap.get("whCd")) || "WH29".equals(detailMap.get("whCd")) || "WH32".equals(detailMap.get("whCd"))){
	    	                                              detailMap.put("worksCd",   "C"); 
	    	                                              detailMap.put("worksCdFrom",   "C");
	    }else {
	    	//  출발지 MES 전송 대상이 아님.
	    	mesIfStrYn = "N";
	    }
	    //set dimsCd
	    detailMap.put("dimsCd", detailMap.get("sPrdtSpec"));

          /*
	     * 코일 여부 Y일 시 
	     * 1) SET 코일철근(param -> productNameCd) : BC
	     * 2) SET sPrdtSpec 마지막 문자 C 제거 후 삽입
	     * */
	    if( "Y".equals(detailMap.get("prdtCoilYn"))) {
			detailMap.put("productNameCd",   "BC"); /* 1) 코일철근 : BC */
		
			String spec = detailMap.get("sPrdtSpec");
			if (spec != null && spec.length() > 0 && spec.charAt(spec.length() - 1) == 'C') { 
				detailMap.put("dimsCd", spec.substring(0, spec.length() - 1)); /* 2) SET sPrdtSpec 마지막 문자 C 제거 후 삽입 */
		    }
	    }else {
			detailMap.put("productNameCd",   "BD"); /* 바철근 : BD */
	    }

	    String prjctCdTo   = detailMap.get("sPrjctCd");
	    String prjctCdFrom = detailMap.get("prjctCd");
	    
	    // site를 가져오는것 추가한다.
		Map<String, String> siteMap = new HashMap<String, String>();
		siteMap.put("coCd", detailMap.get("coCd"));
		siteMap.put("prjctCd", prjctCdFrom);
	    Map<String, String> siteStr =  sd09Mapper.selectMinSite(siteMap);
		siteMap.put("prjctCd", prjctCdTo);	    
	    Map<String, String> siteDes =  sd09Mapper.selectMinSite(siteMap);
	    
	    if("".equals(prjctCdTo)|| "0".equals(prjctCdTo)) {
	    	detailMap.put("siteCdTo",   "R"); /* 유통으로이동 : R */
	    }else {
			detailMap.put("siteCdTo",  siteDes.get("siteCd")); /* 유통으로이동 : R */
	    }
	    if("".equals(prjctCdFrom) || "0".equals(prjctCdFrom)) {
	    	detailMap.put("siteCdFrom",   "R"); /* 유통으로이동 : R */
	    }else {
			detailMap.put("siteCdFrom",  siteStr.get("siteCd")); /* 유통으로이동 : R */
	    }
	    
	    String supCustCdFrom = detailMap.get("clntCd");
	    String supCustCdTo   = detailMap.get("clntCd");
	    detailMap.put("supCustCdFrom",  supCustCdFrom); /* set 재고 주인 */
	    detailMap.put("supCustCdTo",  supCustCdTo);     /* set 이동 재고 주인 */

		// Case 1 공장 -> 공장 이동시
		//        WorksCd = wkoksCdFrom으로 1개 생성
		//        WorksCd = worksCdTo로 1개 생성
		// Case 2 외부 -> 공장 이동시
		//        WorksCd = wkoksCdTo, wkoksCdFrom = null로 1개 생성
		// Case 3 동일공장간 이동시
		//        WorksCd = wkoksCdTo = wkoksCdFrom  1개 생성
		// Case 3 공장 -> 외부 이동시
		//        WorksCd = wkoksCdFrom ,  wkoksCdTo = null 1개 생성
	    
	    String worksCdTo   = detailMap.get("worksCdTo");
	    String worksCdFrom   = detailMap.get("worksCdFrom");
	    
	// 오류수정의 경우 IF TAble에 입력하지 않음.    
	    if (mesIfStrYn == "Y" && !"Y".equals(detailMap.get("errCorecYn"))) {	    
	    	messtockMapper.insertIfMesStockMove(detailMap); 
	    }
	// 출발공장, 도착공장이 다르면 Case 1	 
		 if(!worksCdTo.equals(detailMap.get("worksCdFrom")) &&  mesIfDesYn == "Y" && !"Y".equals(detailMap.get("errCorecYn"))) {
			 detailMap.put("worksCd",   worksCdTo);			 
			 messtockMapper.insertIfMesStockMove(detailMap); 		 
	    }
	    
		return 200;
	}

	@Override
	public void deleteStock(Map<String, String> paramMap) {

		Map<String, String> detailMap = new HashMap<String, String>();
		detailMap.put("coCd", MapUtils.getString(paramMap, "inCoCd"));
		detailMap.put("whCd", MapUtils.getString(paramMap, "inWhCd"));
		detailMap.put("ownerCd", MapUtils.getString(paramMap, "ownerCd"));
		detailMap.put("typCd", MapUtils.getString(paramMap, "inTypCd"));
		detailMap.put("makrCd", MapUtils.getString(paramMap, "makrCd"));
		detailMap.put("prjctCd", MapUtils.getString(paramMap, "inPrjctCd"));
		detailMap.put("impYn", MapUtils.getString(paramMap, "impYn"));
		detailMap.put("prdtCd", MapUtils.getString(paramMap, "inPrdtCd"));
		detailMap.put("prdtSize", MapUtils.getString(paramMap, "inPrdtSize"));
		detailMap.put("prdtSpec", MapUtils.getString(paramMap, "inPrdtSpec"));
		
		detailMap.put("clntCd", MapUtils.getString(paramMap, "clntCd"));
		detailMap.put("stockUpr", MapUtils.getString(paramMap, "stockUpr"));
		detailMap.put("stdUpr", MapUtils.getString(paramMap, "stdUpr"));
		detailMap.put("pchsUpr", MapUtils.getString(paramMap, "pchsUpr"));
		detailMap.put("sellUpr", MapUtils.getString(paramMap, "sellUpr"));
		detailMap.put("userId", MapUtils.getString(paramMap, "userId"));
		detailMap.put("pgmId", MapUtils.getString(paramMap, "pgmId"));
		detailMap.put("moveQty", MapUtils.getString(paramMap, "moveQty"));
		detailMap.put("moveWt", MapUtils.getString(paramMap, "moveWt"));
		detailMap.put("trstNo", MapUtils.getString(paramMap, "trstNo"));
		
		detailMap.put("stockChgCd", "STOCKCHG02");
		detailMap.put("stockQty", "0");
		detailMap.put("stockWt", "0");
		
		
		sm01Mapper.updateStockSell(detailMap);
		
		
		detailMap.put("coCd", MapUtils.getString(paramMap, "outCoCd"));
		detailMap.put("whCd", MapUtils.getString(paramMap, "outWhCd"));
		detailMap.put("ownerCd", MapUtils.getString(paramMap, "ownerCd"));
		detailMap.put("typCd", MapUtils.getString(paramMap, "typCd"));
		detailMap.put("makrCd", MapUtils.getString(paramMap, "makrCd"));
		detailMap.put("prjctCd", MapUtils.getString(paramMap, "prjctCd"));
		detailMap.put("impYn", MapUtils.getString(paramMap, "impYn"));
		detailMap.put("prdtCd", MapUtils.getString(paramMap, "prdtCd"));
		detailMap.put("prdtSize", MapUtils.getString(paramMap, "prdtSize"));
		detailMap.put("prdtSpec", MapUtils.getString(paramMap, "prdtSpec"));
		
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(detailMap);
		
		if(stockInfo == null){
			stockInfo = new HashMap<String, String>();
			stockInfo.put("stockQty", "0");
			stockInfo.put("stockWt", "0");
		}

		// 재고정보 update
		detailMap.put("stockChgCd", "STOCKCHG02");
		detailMap.put("clntCd", stockInfo.get("clntCd"));
		detailMap.put("pchsUpr", stockInfo.get("pchsUpr"));
		detailMap.put("sellUpr", stockInfo.get("sellUpr"));
		detailMap.put("stockUpr", stockInfo.get("stockUpr"));
		detailMap.put("stdUpr", stockInfo.get("stdUpr"));
		
		double stockQty = Double.parseDouble(stockInfo.get("stockQty")) + Double.parseDouble(detailMap.get("moveQty"));
		double stockWt  = Double.parseDouble(stockInfo.get("stockWt"))  + Double.parseDouble(detailMap.get("moveWt"));
		detailMap.put("stockQty", String.valueOf(stockQty));
		detailMap.put("stockWt" , String.valueOf(stockWt));
		
		sm01Mapper.updateStockSell(detailMap);
		
		sm02Mapper.deleteStockHistory(detailMap);
		
	}

}