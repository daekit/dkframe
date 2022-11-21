package com.dksys.biz.user.pp.pp04.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar01.mapper.AR01Mapper;
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.dksys.biz.user.pp.pp04.mapper.PP04Mapper;
import com.dksys.biz.user.pp.pp04.service.PP04Svc;
import com.dksys.biz.user.sd.sd04.mapper.SD04Mapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class PP04Svcmpl implements PP04Svc {
	
    @Autowired
    PP04Mapper pp04Mapper;  
    
    @Autowired
    AR01Mapper ar01Mapper;
    
    @Autowired
    SD04Mapper sd04Mapper;
    
    @Autowired
    AR01Svc ar01Svc;

	@Override
	public int selectMesMtrlRstlFirstCount(Map<String, String> paramMap) {
		return pp04Mapper.selectMesMtrlRstlFirstCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesMtrlRstlFirstList(Map<String, String> paramMap) {
		return pp04Mapper.selectMesMtrlRstlFirstList(paramMap);
	}

	@Override
	public int selectMesMtrlRstlCount(Map<String, String> paramMap) {
		
		return pp04Mapper.selectMesMtrlRstlCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesMtrlRstlList(Map<String, String> paramMap) {
	
		return pp04Mapper.selectMesMtrlRstlList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesAllocVehlDtlList(Map<String, String> paramMap) {
		
		return pp04Mapper.selectMesAllocVehlDtlList(paramMap);
	}   

	@Override
	public int insertMesShipList(Map<String, Object> paramMap) {
		
		//1. 화면에서 선택한 배차번호 목록을 가져와서 loop 실행 
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		
		List<Map<String,String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		for(int i = 0; i < listMap.size(); i++) {
			
			Integer totAmtM = 0;
			Integer totQtyM = 0;
			Integer totWtM = 0;
			
			listMap.get(i).put("issDt", "20"+MapUtils.getString(listMap.get(i), "issDt"));
			listMap.get(i).put("userId", userId);
			listMap.get(i).put("loginId", userId);
			listMap.get(i).put("userNm", userNm);
			listMap.get(i).put("pgmId", pgmId);
			listMap.get(i).put("erpTransYn", "Y");
			
			
			// 출하요청서를 생성하기 전에 상차지시번호로 출하요청서가 만들어져있는지 확인하는 로직
			// Map<String, Object> ar01Map = pp04Mapper.selectAr01MListLoadNo(listMap.get(i));
			
			// 해당 현장코드에 따른 현장 정보 가져오기
			Map<String, String> siteMap = pp04Mapper.selectSite(listMap.get(i));
			listMap.get(i).putAll(siteMap);
			
			// 한 현장의 일자에 따라서는 여러 상차지시번호가 존재할 수 있기 때문에, 상차지시번호를 for문 돌면서 where절에 넣을 수 있도록 형태 가공
			String loadOrgNo = "";
			String loadOrgNoGroup = MapUtils.getString(listMap.get(i), "loadOrgNoGroup");
			
			String[] loadOrgNoList = loadOrgNoGroup.split(",");
			for(int l = 0; l < loadOrgNoList.length; l++) {
				loadOrgNo += "'" + loadOrgNoList[l] + "'";
				
				if(l != loadOrgNoList.length-1) {
					loadOrgNo += ",";
				}
			}
			
			listMap.get(i).put("loadOrgNo", loadOrgNo);

			

			// 새로운 출하요청서를 생성하기 위해서 출하요청서 시퀀스를 하나 생성
			Map<String, String> ar01MSeq = pp04Mapper.selectAr01MSeq();
			
			// 상차지시번호 묶인 그룹에 따라 출하요청서 상세 테이블에 넣을 데이터 출력 - 강종/길이별 총 수량과 중량
			List<Map<String, String>> stockListMap = pp04Mapper.selectMesMtrlRstlList(listMap.get(i));
			
			// 현장, 날짜에 따른 강종/길이별 데이터를 for문 돌면서 출하요청서 상세 테이블에 INSERT 
			// 109번라인에서 추출한 새로 만들 출하요청서 시퀀스도 같이 입력
			for(Map<String, String> stockMap : stockListMap) {
				Map<String, String> stockInfo = pp04Mapper.selectStockInfo(stockMap);
				listMap.get(i).putAll(stockMap);
				listMap.get(i).putAll(stockInfo);
				Integer issWgt = MapUtils.getInteger(listMap.get(i), "issWgt");
				Integer prodPcsCnt = MapUtils.getInteger(listMap.get(i), "prodPcsCnt");
				// Integer shipUpr = MapUtils.getInteger(listMap.get(i), "prdtUpr");
				Integer shipUpr = MapUtils.getInteger(listMap.get(i), "shipUpr");
				Integer totAmtD = prodPcsCnt*shipUpr;
				totAmtM += totAmtD;
				totQtyM += prodPcsCnt;
				totWtM += issWgt;
				
				listMap.get(i).put("totAmt", Integer.toString(totAmtD));
				listMap.get(i).put("shipSeq", ar01MSeq.get("nextval"));
				
				/*
				if(listMap.get(i).get("makrCd").equals("MAKR01")) {
					listMap.get(i).put("impYn", "N");
				}else {
					listMap.get(i).put("impYn", "Y");
				}
				*/
				
				listMap.get(i).put("impYn", "N");
				pp04Mapper.insertShipDetail(listMap.get(i));
			}
			
			listMap.get(i).put("totAmt", Integer.toString(totAmtM));
			listMap.get(i).put("totQty", Integer.toString(totQtyM));
			listMap.get(i).put("totWt", Integer.toString(totWtM));
			
			// 새로운 출하요청서 INSERT 작업
			listMap.get(i).put("odrRmk", "0");
			
			// 출하요청서는 개별로 수입여부와 제조사를 관리하므로 첫번째를 추출하여 주문 insert
			listMap.get(i).put("impYn", "N");
			listMap.get(i).put("makrCd", "MAKR09");
			// sd04Mapper.insertOrder(listMap.get(i));
			
			Map<String, String> orderMap = pp04Mapper.insertOrder(listMap.get(i));
			Map<String, String> orderDtlMap = pp04Mapper.insertOrderDetail(listMap.get(i));
			
			listMap.get(i).put("odrSeq", orderMap.get("nextval"));
			listMap.get(i).put("ordrgSeq", orderDtlMap.get("nextval"));
		
			
			// 현장 검색으로도 값이 없는 데이터를 추가적으로 null 넣어주는 로직
			List<String> containList = new ArrayList<>();
			containList.add("prjctCd");
			containList.add("telNo");
			containList.add("siteAddrZip");
			containList.add("siteAddr");
			containList.add("siteAddrSub");
			containList.add("vehlNo");
			
			for(int m=0; m<containList.size(); m++) {
				if(!listMap.get(i).containsKey(containList.get(m))) {
					listMap.get(i).put(containList.get(m), "");
				}
			}
			
			listMap.get(i).put("erpUpdateFlag", "Y");
			
			// 출하요청서 메인 테이블 INSERT
			pp04Mapper.insertShip(listMap.get(i));
			
			
			pp04Mapper.updateMesErpFlag(listMap.get(i));
			
			
			
			
		}
		return 0;
	}

	@Override
	public int selectBilgNoCnt(Map<String, Object> paramMap) {
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		int cnt = 0;
		List<Map<String,String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		for(int i = 0; i < listMap.size(); i++) {
			listMap.get(i).put("userId", userId);
			listMap.get(i).put("userNm", userNm);
			listMap.get(i).put("pgmId", pgmId);
			cnt = pp04Mapper.selectBilgNoCnt(listMap.get(i));
			if (cnt > 0)
				break;
		}
		return cnt;
	}
	
	@Override
	public int deleteMesShipList(Map<String, Object> paramMap) {
		
		//1. 화면에서 선택한 배차번호 목록을 가져와서 loop 실행 
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		
		List<Map<String,String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		for(int i = 0; i < listMap.size(); i++) {
			listMap.get(i).put("userId", userId);
			listMap.get(i).put("userNm", userNm);
			listMap.get(i).put("pgmId", pgmId);
			listMap.get(i).put("erpTransYn", "N");
			pp04Mapper.deleteSellTrst(listMap.get(i));
			pp04Mapper.deleteMesDetailList(listMap.get(i));
			pp04Mapper.deleteMesList(listMap.get(i));
			pp04Mapper.updateMesMtrlRslt(listMap.get(i));
		}
		return 0;
	}

	@Override
	public List<Map<String, String>> daliyAccessList(Map<String, String> paramMap) {
		return pp04Mapper.daliyAccessList(paramMap);
	}
	
	/*
	    @Override
	public int selectMesShipCount(Map<String, String> paramMap) {
		return pp04Mapper.selectMesShipCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesShipList(Map<String, String> paramMap) {
		return pp04Mapper.selectMesShipList(paramMap);
	}
		


	@Override
	public int updatMesSHip(Map<String, String> param) {
		return pp04Mapper.updatMesSHip(param);
	}
	
	@Override
	public int deleteMesShip(Map<String, String> param) {
		return pp04Mapper.deleteMesShip(param);
	}
	@Override
	public int updatMesSHipConfirm(Map<String, String> param) {
		return pp04Mapper.updatMesSHipConfirm(param);
	}
	
	
	
	

	 */



/*
	@Override
	public List<Map<String, String>> selectMesAllocVehlList(Map<String, String> param) {
		
		List<Map<String, String>> allocVehlList = pp04Mapper.selectMesAllocVehlList(param);
		for(Map<String, String> detailMap : allocVehlList) {
			List<Map<String, String>> allocVehlDtlList =pp04Mapper.selectMesAllocVehlDtlList(param);
			for(Map<String, String> allocVehlDtl : allocVehlDtlList){
				// 개별 실적을 정리해서 I/F테이블에 입력한다.
//			/*  facCd,
//				allocVehlNo,
//			 	ordPackWgt,
//				baseDd,
//				lossRate,
//				custCd,
//				custNm,
//				ordCompCd,
//				dimsCd,
//				*/
		//		ordNo,		ordLineNo 을 활용해서 TB_SD04M01, TB_SD04M01_MES, TB_SD04M01,TB_SD04M01_MES에서 필요자료를 가져온다.
				
//				Map<String, String> erpOrder = null;
		//		Map<String, String> erpOrder = pp04Mapper.selectMesOrder(param);
//		/*  주문에서 필요한 자료는 전부 여기에서 가져온다.		
				
	//			productNameCd,
				 
				// allocVehlDtl.put("mesCertiNo",   );	/**/
//				 allocVehlDtl.put("mesFtrCd",allocVehlDtl.get("worksCd"));	/* MES 공장 구분*/
//				 allocVehlDtl.put("cfYn",  "N"  );	                       /* ERP 반영 여부*/
				// allocVehlDtl.put("coCd",   );	/*회사코드*/
//				 allocVehlDtl.put("selpchCd",  "SELPCH02"  );	/*매입/매출구분*/
//				 allocVehlDtl.put("trstDt",allocVehlDtl.get("baseDd") );	/*거래일자*/
//				 allocVehlDtl.put("taxivcCoprt",   );	/*세금계산서발행법인*/
//				 allocVehlDtl.put("trstClntCd",   );	/*거래내역 거래처 코드*/
//				 allocVehlDtl.put("trstPrdtCd",   );	/*제품코드*/
//				 allocVehlDtl.put("ownerCd",   );	/*재고주체구분*/
//				 allocVehlDtl.put("impYn",   );	/*수입구분*/
//				 allocVehlDtl.put("typCd",   );	/*판매유형*/
//				 allocVehlDtl.put("trspTypCd",   );	/*입출고거래유형  */
//				 allocVehlDtl.put("prdtLen",   );	/*제품길이*/
//				 allocVehlDtl.put("odrNo",   );	    /*주문서 번호*/
//				 allocVehlDtl.put("salesMng",   );	/*영업당당자*/
//				 allocVehlDtl.put("prjctCd",   );	/*프로젝트 코드*/
//				 allocVehlDtl.put("makrCd",   );	/*제조사*/
//				 allocVehlDtl.put("whCd",   ) ;	    /*창고구분*/
//				 allocVehlDtl.put("mngTel",   );	/*담당자 연락처*/
//				 allocVehlDtl.put("dlvrDttm",   );	/*납품일자시간*/
//				 allocVehlDtl.put("dirtrsYn",   );	/*직접운송구분*/
//				 allocVehlDtl.put("prdtDiv",   );	/*제품구분*/
//				 allocVehlDtl.put("prdtStkn",   );	/*제품강종*/
//				 allocVehlDtl.put("prdtSize",   );	/*사이즈*/
//				 allocVehlDtl.put("prdtUnit",   );	/*제품단위*/
//				 allocVehlDtl.put("prdtUpr",   );	/*제품기준단가*/
//				 allocVehlDtl.put("stockUpr",   );	/*재고단가*/
//				 allocVehlDtl.put("pchsUpr",   );	/*최종매입단가*/
//				 allocVehlDtl.put("sellUpr",   );	/*최종매츨단가*/
//				 allocVehlDtl.put("trstQty", allocVehlDtl.get("ordPackWgt"));	/*거래지시수량*/
//				 allocVehlDtl.put("trstWt",  allocVehlDtl.get("ordPackWgt"));	/*거래지시중량*/
//				 allocVehlDtl.put("trstUpr",   );	/*거래지시단가*/
//				 allocVehlDtl.put("trstAmt",   );	/*거래지시금액*/
//				 allocVehlDtl.put("realTrstQty", allocVehlDtl.get("ordPackWgt")  );	/*실거래수량*/
//				 allocVehlDtl.put("realTrstWt",  allocVehlDtl.get("ordPackWgt"));	/*실거래중량*/
//				 allocVehlDtl.put("realTrstUpr",   );	/*실거래단가*/
//				 allocVehlDtl.put("realTrstAmt",   );	/*실거래금액*/
//				 allocVehlDtl.put("bilgQty", allocVehlDtl.get("ordPackWgt") );	/*청구수량*/
//				 allocVehlDtl.put("bilgWt",  allocVehlDtl.get("ordPackWgt") );	/*청구중량*/
//				 allocVehlDtl.put("bilgUpr",   );	/*청구단가*/
//				 allocVehlDtl.put("bilgAmt",   );	/*청구금액*/
//				 allocVehlDtl.put("trstDcAmt",   );	/*할인금액*/
//				 allocVehlDtl.put("etcAmt",   );	/*기타금액*/
//				 allocVehlDtl.put("trspRmk",   );	/*거래 비고*/
//				 allocVehlDtl.put("transAmt",   );	/*운반비*/
//				 allocVehlDtl.put("shipVehNo", allocVehlDtl.get("vehlNo") );	/*차량번호*/
//				 allocVehlDtl.put("clntNm",   );	/*거래처명*/
//				 allocVehlDtl.put("bilgCertNo",   );	/*청구서번호*/
//				 allocVehlDtl.put("lossRate",   );	/*절감율*/
//				 allocVehlDtl.put("odrCreatDiv",   );	/*오더생성구분*/
//				 allocVehlDtl.put("creatId",   );	/*생성자*/
//				 allocVehlDtl.put("creatPgm",   );	/*생성프로그램ID*/
//				 allocVehlDtl.put("creatDttm",   );	/*생성일시*/
//				 allocVehlDtl.put("udtId",   );	/*최종변경자*/
//				 allocVehlDtl.put("udtPgm",   );	/*최종수정프로그램ID*/
//				 allocVehlDtl.put("udtDttm",   );	/*최종변경일시*/
//				 allocVehlDtl.put("salesAreaCd",   );	/*판매지역 코드*/
//				 allocVehlDtl.put("siteCd",   );	/*현장 코드*/
//				 allocVehlDtl.put("bilgVatAmt",   );	/*청구부가세금액*/
//				 allocVehlDtl.put("prdtSpec",   );	/*제품스펙*/

//			}
//			pp04Mapper.updateMesMtrlRslt(param);
//		}
//		return pp04Mapper.selectMesAllocVehlDtlList(param);
//	}
	
}