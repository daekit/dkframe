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
	public int selectMesMtrlRstlUnGroupListCount(Map<String, String> paramMap) {
		return pp04Mapper.selectMesMtrlRstlUnGroupListCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMesMtrlRstlUnGroupList(Map<String, String> paramMap) {
		return pp04Mapper.selectMesMtrlRstlUnGroupList(paramMap);
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

	/*
	// 20221125 넘기는 것 그대로 생성하는 로직 백업해두기
	@Override
	public int insertMesShipList(Map<String, Object> paramMap) {
		
		//1. 화면에서 선택한 배차번호 목록을 가져와서 loop 실행 
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String issDt = String.valueOf(paramMap.get("issDt"));
		
		List<Map<String,String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		for(int i = 0; i < listMap.size(); i++) {
			
			Integer totAmtM = 0;
			Integer totQtyM = 0;
			Integer totWtM = 0;
			
			listMap.get(i).put("issDt", issDt);
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
			// 새로 추출한 새로운 출하요청서 시퀀스도 같이 입력
			for(Map<String, String> stockMap : stockListMap) {
				Map<String, String> stockInfo = pp04Mapper.selectStockInfo(stockMap);
				listMap.get(i).putAll(stockMap);
				listMap.get(i).putAll(stockInfo);
				
				// 중량도 개수와 동일하기 1:1로 변경하도록 수정
				// Integer issWgt = MapUtils.getInteger(listMap.get(i), "issWgt");
				Integer issWgt = MapUtils.getInteger(listMap.get(i), "prodPcsCnt");
				Integer prodPcsCnt = MapUtils.getInteger(listMap.get(i), "prodPcsCnt");
				// Integer shipUpr = MapUtils.getInteger(listMap.get(i), "prdtUpr");
				Integer shipUpr = MapUtils.getInteger(listMap.get(i), "shipUpr");
				Integer totAmtD = prodPcsCnt*shipUpr;
				totAmtM += totAmtD;
				totQtyM += prodPcsCnt;
				totWtM += issWgt;
				
				listMap.get(i).put("totAmt", Integer.toString(totAmtD));
				listMap.get(i).put("shipSeq", ar01MSeq.get("nextval"));
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
	*/
	
	
	
	// 20221128 넘기는 데이터를 다음 것과 확인해서 insert 하기
	@Override
	public int insertMesShipList(Map<String, Object> paramMap) {
		
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String issDt = String.valueOf(paramMap.get("issDt"));
		String loadOrgNo = "";
		String siteCd = "";
		List<String> loadOrgNoList = new ArrayList<String>(); 
		List<Map<String, String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		
		
		// 화면에서 넘겨준 list를 돌면서 다음 현장과 같은지 확인하고 같으면 병합, 다르면 따로 출하요청서를 생성하는 로직 수행
		for(int i = 0; i < listMap.size(); i++) {
			
			String loadOrgNoGroup = MapUtils.getString(listMap.get(i), "loadOrgNoGroup");
			siteCd = MapUtils.getString(listMap.get(i), "siteCd");
			
			// 현재의 for문에서 listMap의 마지막 인자가 아닐 때
			if(i != listMap.size()-1) {
				loadOrgNoList.add(loadOrgNoGroup);
				
				// 다음 현장 코드와 현재의 현장 코드가 동일한지 확인 - 다음 현장코드와 다른 상태
				if(!siteCd.equals(MapUtils.getString(listMap.get(i+1), "siteCd"))) {
					
					for(int l = 0; l < loadOrgNoList.size(); l ++) {
						
						String[] splitList = loadOrgNoList.get(l).split(",");
						
						for(int s = 0; s < splitList.length; s++) {
							loadOrgNo += "'" + splitList[s] + "'";
							
							if(s != splitList.length-1) {
								loadOrgNo += ",";
							}
						}
						
						if(l != loadOrgNoList.size()-1) {
							loadOrgNo += ",";
						}
						
					}
					
					Integer totAmtM = 0;
					Integer totQtyM = 0;
					Integer totWtM = 0;
					
					listMap.get(i).put("issDt", issDt);
					listMap.get(i).put("userId", userId);
					listMap.get(i).put("loginId", userId);
					listMap.get(i).put("userNm", userNm);
					listMap.get(i).put("pgmId", pgmId);
					listMap.get(i).put("erpTransYn", "Y");
					
					
					// 출하요청서를 생성하기 전에 상차지시번호로 출하요청서가 만들어져있는지 확인하는 로직 - 나중에 LIKE 절로 추가해야할듯
					// Map<String, Object> ar01Map = pp04Mapper.selectAr01MListLoadNo(listMap.get(i));
					
					// 해당 현장코드에 따른 현장 정보 가져오기
					Map<String, String> siteMap = pp04Mapper.selectSite(listMap.get(i));
					listMap.get(i).putAll(siteMap);
					
					listMap.get(i).put("loadOrgNo", loadOrgNo);
					

					// 새로운 출하요청서를 생성하기 위해서 출하요청서 시퀀스를 하나 생성
					Map<String, String> ar01MSeq = pp04Mapper.selectAr01MSeq();
					
					// 상차지시번호 묶인 그룹에 따라 출하요청서 상세 테이블에 넣을 데이터 출력 - 강종/길이별 총 수량과 중량
					List<Map<String, String>> stockListMap = pp04Mapper.selectMesMtrlRstlList(listMap.get(i));
					
					// 현장, 날짜에 따른 강종/길이별 데이터를 for문 돌면서 출하요청서 상세 테이블에 INSERT 
					// 새로 추출한 새로운 출하요청서 시퀀스도 같이 입력
					for(Map<String, String> stockMap : stockListMap) {
						stockMap.put("CO_CD", MapUtils.getString(listMap.get(i), "coCd"));
						Map<String, String> stockInfo = pp04Mapper.selectStockInfo(stockMap);
						listMap.get(i).putAll(stockMap);
						listMap.get(i).putAll(stockInfo);
						
						// 중량도 개수와 동일하기 1:1로 변경하도록 수정
						Integer issWgt = MapUtils.getInteger(listMap.get(i), "issWgt");
						Integer prodPcsCnt = MapUtils.getInteger(listMap.get(i), "issWgt");
						Integer shipUpr = MapUtils.getInteger(listMap.get(i), "shipUpr");
						Integer totAmtD = prodPcsCnt*shipUpr;
						totAmtM += totAmtD;
						totQtyM += prodPcsCnt;
						totWtM += issWgt;
						
						listMap.get(i).put("totAmt", Integer.toString(totAmtD));
						listMap.get(i).put("shipSeq", ar01MSeq.get("nextval"));
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
					
					// loadOrgNo에서 loadOrgNoGroup으로 치환해서 다시 입력하기
					loadOrgNo = loadOrgNo.replaceAll("'", "");
					listMap.get(i).put("loadOrgNoGroup", loadOrgNo);
					
					// 출하요청서 메인 테이블 INSERT
					pp04Mapper.insertShip(listMap.get(i));
					pp04Mapper.updateMesErpFlag(listMap.get(i));
					
					// loadOrgNoList, loadOrgNo 초기화
					loadOrgNoList = new ArrayList<String>();
					loadOrgNo = "";
				}
				
			// 1개가 있을 때는 첫번째로, 여러개가 있을 때는 마지막에 도는 함수
			} else {
				loadOrgNoList.add(loadOrgNoGroup);
				
				for(int l = 0; l < loadOrgNoList.size(); l ++) {
					String[] splitList = loadOrgNoList.get(l).split(",");
					
					for(int s = 0; s < splitList.length; s++) {
						loadOrgNo += "'" + splitList[s] + "'";
						
						if(s != splitList.length-1) {
							loadOrgNo += ",";
						}
					}
					
					if(l != loadOrgNoList.size()-1) {
						loadOrgNo += ",";
					}
				}
				
				Integer totAmtM = 0;
				Integer totQtyM = 0;
				Integer totWtM = 0;
				
				listMap.get(i).put("issDt", issDt);
				listMap.get(i).put("userId", userId);
				listMap.get(i).put("loginId", userId);
				listMap.get(i).put("userNm", userNm);
				listMap.get(i).put("pgmId", pgmId);
				listMap.get(i).put("erpTransYn", "Y");
				
				
				// 출하요청서를 생성하기 전에 상차지시번호로 출하요청서가 만들어져있는지 확인하는 로직 - 나중에 LIKE 절로 추가해야할듯
				// Map<String, Object> ar01Map = pp04Mapper.selectAr01MListLoadNo(listMap.get(i));
				
				// 해당 현장코드에 따른 현장 정보 가져오기
				Map<String, String> siteMap = pp04Mapper.selectSite(listMap.get(i));
				listMap.get(i).putAll(siteMap);
				
				listMap.get(i).put("loadOrgNo", loadOrgNo);
				
				// 새로운 출하요청서를 생성하기 위해서 출하요청서 시퀀스를 하나 생성
				Map<String, String> ar01MSeq = pp04Mapper.selectAr01MSeq();
				
				// 상차지시번호 묶인 그룹에 따라 출하요청서 상세 테이블에 넣을 데이터 출력 - 강종/길이별 총 수량과 중량
				List<Map<String, String>> stockListMap = pp04Mapper.selectMesMtrlRstlList(listMap.get(i));
				
				// 현장, 날짜에 따른 강종/길이별 데이터를 for문 돌면서 출하요청서 상세 테이블에 INSERT 
				// 새로 추출한 새로운 출하요청서 시퀀스도 같이 입력
				for(Map<String, String> stockMap : stockListMap) {
					stockMap.put("CO_CD", MapUtils.getString(listMap.get(i), "coCd"));
					Map<String, String> stockInfo = pp04Mapper.selectStockInfo(stockMap);
					listMap.get(i).putAll(stockMap);
					listMap.get(i).putAll(stockInfo);
					
					// 중량도 개수와 동일하기 1:1로 변경하도록 수정
					Integer issWgt = MapUtils.getInteger(listMap.get(i), "issWgt");
					Integer prodPcsCnt = MapUtils.getInteger(listMap.get(i), "issWgt");
					Integer shipUpr = MapUtils.getInteger(listMap.get(i), "shipUpr");
					Integer totAmtD = prodPcsCnt*shipUpr;
					totAmtM += totAmtD;
					totQtyM += prodPcsCnt;
					totWtM += issWgt;
					
					listMap.get(i).put("totAmt", Integer.toString(totAmtD));
					listMap.get(i).put("shipSeq", ar01MSeq.get("nextval"));
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
				
				// loadOrgNo에서 loadOrgNoGroup으로 치환해서 다시 입력하기
				loadOrgNo = loadOrgNo.replaceAll("'", "");
				listMap.get(i).put("loadOrgNoGroup", loadOrgNo);
				
				// 출하요청서 메인 테이블 INSERT
				pp04Mapper.insertShip(listMap.get(i));
				pp04Mapper.updateMesErpFlag(listMap.get(i));
			
			}
		}
		return 200;
	}
	
	// 20221128 넘기는 데이터를 다음 것과 확인해서 insert 하기
	@Override
	public int compulsionMesUpdate(Map<String, Object> paramMap) {
		
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String issDt = String.valueOf(paramMap.get("issDt"));
		String loadOrgNo = "";
		String siteCd = "";
		List<String> loadOrgNoList = new ArrayList<String>(); 
		List<Map<String, String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		
		// 화면에서 넘겨준 list를 돌면서 다음 현장과 같은지 확인하고 같으면 병합, 다르면 따로 출하요청서를 생성하는 로직 수행
		for(int i = 0; i < listMap.size(); i++) {
			
			String loadOrgNoGroup = MapUtils.getString(listMap.get(i), "loadOrgNoGroup");
			String[] splitList = loadOrgNoGroup.split(",");
			String mergeLoadOrgNo = "";
			
			for(int s = 0; s < splitList.length; s++) {
				mergeLoadOrgNo += "'" + splitList[s] + "',";
			}
			
			loadOrgNo += mergeLoadOrgNo;
		}

		String lastStringCheck = loadOrgNo.substring(loadOrgNo.length()-1);
		
		if(lastStringCheck.equals(",")) {
			loadOrgNo = loadOrgNo.substring(0, loadOrgNo.length()-1);
		}
		
		Map<String, Object> checkMap = new HashMap<String, Object>();

		checkMap.put("mesFtrCd", listMap.get(0).get("mesFtrCd"));
		checkMap.put("loadOrgNo", loadOrgNo);
		int updateCount = 500;
		
		try {
			
			Map<String, Object> checkFlagMap = pp04Mapper.checkProgressFlag(checkMap);
			int countFlagY = MapUtils.getInteger(checkFlagMap, "countFlagY");
			
			if(countFlagY == 0) {
				checkMap.put("erpUpdateLockFlag", "Y");
				pp04Mapper.updateProgressLockFlag(checkMap);
			}else {
				checkMap.put("erpUpdateLockFlag", "N");
				pp04Mapper.updateProgressLockFlag(checkMap);
				return updateCount;
			}

			Map<String, String> updateMap = new HashMap<String, String>();
			
			updateMap.put("erpUpdateFlag", "Y");
			updateMap.put("mesFtrCd", listMap.get(0).get("mesFtrCd"));
			updateMap.put("loadOrgNo", loadOrgNo);
			
	 		updateCount = pp04Mapper.compulsionMesUpdate(updateMap);
	 		checkMap.put("erpUpdateLockFlag", "N");
			pp04Mapper.updateProgressLockFlag(checkMap);
			
		}catch (Exception e) {
			checkMap.put("erpUpdateLockFlag", "N");
			pp04Mapper.updateProgressLockFlag(checkMap);
		}
		
		return updateCount;
	}
	
	// 20221219 MES 강제 취소 처리 로직 추가
	@Override
	public int compulsionMesCancel(Map<String, Object> paramMap) {
		
		String userId = String.valueOf(paramMap.get("userId"));
		String userNm = String.valueOf(paramMap.get("userNm"));
		String pgmId = String.valueOf(paramMap.get("pgmId"));
		String issDt = String.valueOf(paramMap.get("issDt"));
		String loadOrgNo = "";
		String siteCd = "";
		List<String> loadOrgNoList = new ArrayList<String>(); 
		List<Map<String, String>> listMap = (List<Map<String,String>>)paramMap.get("list");
		
		// 화면에서 넘겨준 list를 돌면서 다음 현장과 같은지 확인하고 같으면 병합, 다르면 따로 출하요청서를 생성하는 로직 수행
		for(int i = 0; i < listMap.size(); i++) {
			
			String loadOrgNoGroup = MapUtils.getString(listMap.get(i), "loadOrgNoGroup");
			String[] splitList = loadOrgNoGroup.split(",");
			String mergeLoadOrgNo = "";
			
			for(int s = 0; s < splitList.length; s++) {
				mergeLoadOrgNo += "'" + splitList[s] + "',";
			}
			
			loadOrgNo += mergeLoadOrgNo;
		}

		String lastStringCheck = loadOrgNo.substring(loadOrgNo.length()-1);
		
		if(lastStringCheck.equals(",")) {
			loadOrgNo = loadOrgNo.substring(0, loadOrgNo.length()-1);
		}
		
		Map<String, Object> checkMap = new HashMap<String, Object>();

		checkMap.put("mesFtrCd", listMap.get(0).get("mesFtrCd"));
		checkMap.put("loadOrgNo", loadOrgNo);
		int updateCount = 500;
		
		try {
			
			// 선택된 출하리스트 중 출하요청서가 생성된 건이 있는지 확인하는 로직
			Map<String, Object> checkShipMap = pp04Mapper.checkShipSeq(checkMap);
			int countShipY = MapUtils.getInteger(checkShipMap, "countShipY");
			
			if(countShipY != 0) {
				updateCount = 300;
				return updateCount;
			}
			
			// 현재 작업중인 출하리스트 번호가 들어있는지 확인하는 로직
			Map<String, Object> checkFlagMap = pp04Mapper.checkProgressFlag(checkMap);
			int countFlagY = MapUtils.getInteger(checkFlagMap, "countFlagY");
			
			if(countFlagY == 0) {
				checkMap.put("erpUpdateLockFlag", "Y");
				pp04Mapper.updateProgressLockFlag(checkMap);
			}else {
				checkMap.put("erpUpdateLockFlag", "N");
				pp04Mapper.updateProgressLockFlag(checkMap);
				return updateCount;
			}

			Map<String, String> updateMap = new HashMap<String, String>();
			
			updateMap.put("erpUpdateFlag", "N");
			updateMap.put("mesFtrCd", listMap.get(0).get("mesFtrCd"));
			updateMap.put("loadOrgNo", loadOrgNo);
			
	 		updateCount = pp04Mapper.compulsionMesUpdate(updateMap);
	 		checkMap.put("erpUpdateLockFlag", "N");
			pp04Mapper.updateProgressLockFlag(checkMap);
			
		}catch (Exception e) {
			checkMap.put("erpUpdateLockFlag", "N");
			pp04Mapper.updateProgressLockFlag(checkMap);
		}
		
		return updateCount;
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
	
}