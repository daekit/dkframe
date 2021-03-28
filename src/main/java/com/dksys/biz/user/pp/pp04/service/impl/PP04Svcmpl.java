package com.dksys.biz.user.pp.pp04.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.pp.pp04.mapper.PP04Mapper;
import com.dksys.biz.user.pp.pp04.service.PP04Svc;

@Service
public class PP04Svcmpl implements PP04Svc {
	
    @Autowired
    PP04Mapper pp04Mapper;  


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
	public int insertMesShipList(Map<String, String> paramMap) {
		
		//1. 화면에서 선택한 배차번호 목록을 가져와서 loop 실행 
		//2. 각 배차별로 주문이 연결되엇는지 확인
		//3. 각 배차별로 출하요청서 생성
		//4. 각 출하요청서별 매출 생성

		// TODO Auto-generated method stub
		
		
		// 생성완료된 배차는 완료 표기
		pp04Mapper.updateMesMtrlRslt(paramMap);
		
		
		return 0;
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