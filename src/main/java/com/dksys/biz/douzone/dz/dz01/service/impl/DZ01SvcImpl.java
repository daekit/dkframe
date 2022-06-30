package com.dksys.biz.douzone.dz.dz01.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm06.service.CM06Svc;
import com.dksys.biz.douzone.dz.dz01.mssqlmapper.DZ01Mapper;
import com.dksys.biz.douzone.dz.dz01.service.DZ01Svc;

@Service
@Transactional(value="mssqlTransactionManager", rollbackFor = Exception.class)
public class DZ01SvcImpl implements DZ01Svc {
	@Autowired
	DZ01Mapper dz01Mapper;
	
	@Autowired
	CM06Svc cm06Svc;
	
	@Override
	public List<Map<String, String>> testSelect(Map<String, String> paramMap) {
		List<Map<String, String>> testList = dz01Mapper.testSelect(paramMap);
		return testList;
	}

	@Override
  	public Map<String, String> getAcctAm(Map<String, String> paramMap){
		Map<String, String> acctInfo = dz01Mapper.getAcctAm(paramMap);
		return acctInfo;
	}
	
	@Override
	public Map<String, String> checkTrstCertiNo(Map<String, String> paramMap){
		Map<String, String> TrstCertiNo = dz01Mapper.checkTrstCertiNo(paramMap);
		return TrstCertiNo;
	}
	
	@Override
	public int getCountInSeq(Map<String, String> paramMap) {
		int InSeq = dz01Mapper.getCntSeq(paramMap);
		return InSeq;
	}

	@Override
	public String getTrCd(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String trCd = dz01Mapper.getTrCd(paramMap);
		return trCd ;
	}
    @SuppressWarnings("null")
	@Override
  	public int  dzInsert(Map<String, String> paramMap){
	
	  //---------------------------1차 INSERT--------------------------------------------------------------------------
    	
      String etrdpsAmt = paramMap.get("etrdpsAmt");
    
      int in_sq = 0 ;		             // 거래순번
      int ln_sq = 2 ; 			         // 분개라인 순번 순번은 총 2번까지 생성됨 (수는 변경 될 수 있음)
      String div_cd = "1000";     		 // 회계단위 
      in_sq = getCountInSeq(paramMap);   // IN_SQ  - 거래순번  최근 차수를 가져오는 query 호출  +1 로 들고 온다 
      String tr_cd ="";   				 // 은행 거래처 코드 
      
      //공통으로 사용하는 데이터 셋팅 
      paramMap.put("inSq",Integer.toString(in_sq));  
      paramMap.put("divCd",div_cd);		 // 회계 단위 셋팅  1000
      
      tr_cd = getTrCd(paramMap);  		 // 은행 거래처 코드 를 들고옴 ( 계좌 정보가 있는지 확인 )
      String bkAct 		= paramMap.get("bkAct");
      String setleTypCd = paramMap.get("setleTypCd");
      if (bkAct != null) { 	  // 은행 계좌가 null 이 아니고 tr_cd 를 검색 했는데 더존 코드가 없을 시 
    	
          if (tr_cd == null || tr_cd.trim().isEmpty()) {  // 더존에 관련 계좌가 없기 때문에 return 해줌 
        	  // error   더존 은행 거래저가 없습니다 .
        	  return 20000;
          }

      }		 
      
      /*
       *   차변/대변 데이터 생성  차변 구분 코드 dccrfg = 3  / 대변 4 
       *   은행 지급계좌가 있을 시 은행계좌 를 검색해서 bkact(은행 계좌 )를 더존 db 에서 검색 후 tr_cd 값을 채워주어야함 
       *   in_sq = 전표 묶음 행번  // ln_sq 전표 det 행번   ex)  in_sq = 1 일 때  ln_sq 차변 1 대변 2 를 물고 간다     
       *   dzCode 코드는 차변일대 1번째 code  대변일때 dzCode2 를 가져 와서 넣어준다  
       *   % 수금이고 어음일때 더존 계정 코드 수정 필요  10800 ->11000 으로 변경 해야됨 
       *   %상계 (지급보증) 일때 위차면 금액 -  대변 아래쪽도 차변 처리 ( 대변 없음 ) 상계 지급 보증 코드 =ETRDPS04
       */
      
      String etrdps_type = paramMap.get("etrdpsTyp");
      
      for (int i = 1 ; i <=ln_sq; i++) {
    	  
    	  	if( i == 1 ) { 	// 차변 전표 생성 
    	  		
    	  		
    	  			
	    	  		 if (setleTypCd.equals("PMNTMTD02")) { //어음 일때 데이터 셋팅 
	    	  	    	 
	    	  			paramMap.put("dzCode","11000"); 				//어음일때 어음 코드 셋팅 
	    	  			paramMap.put("frDt",paramMap.get("etrdpsDt"));  //어음일때 시작일 
	    	  			paramMap.put("toDt",paramMap.get("exprtnDt"));  //어음일때 만기일 
	    	  			paramMap.put("ctNb",paramMap.get("bilNo"));     //어음일때 어음번호 셋팅 
	    	  			
	    	  	     }else if (etrdps_type.equals("ETRDPS04")) { //상계 지급 보충일때는 차변 값 - 값으로 전환 
	    	  	    	 
	    	  	    	int etr_amt =Integer.parseInt(paramMap.get("etrdpsAmt"))*-1;  //string  int parse 음수 전환 
	    	  	    	paramMap.put("etrdpsAmt", Integer.toString(etr_amt));   
	    	  	    	
	    	  	     }
    	  		
    	  			paramMap.put("lnSq", Integer.toString(i));
    	  			paramMap.put("drcrFg", "3");    	  			
    	  			
    	  	}else {  	// 대변데이터 생성  
    	  			 	// 은행지급 계좌가 있을시  거래처 제외    	  		
	    	  			if (bkAct == null || bkAct.trim().isEmpty()) {
	    	  				
	    	  			}else {
	    	  				
	    	  					paramMap.put("crn","");     	// 은행계좌가 있을시 사업자 코드 NULL
		    	  			    paramMap.put("trCd",tr_cd); 	// 은행 거래처 코드 넣어줌 	 
	    	  					 
	    	  			}
	    	  			
    	  		    paramMap.put("lnSq", Integer.toString(i));
    	  		    
    	  		    if (etrdps_type.equals("ETRDPS04")) {	 //상계 지급 보충일때
    	  		    	
        	  		    paramMap.put("drcrFg", "3");         //차변 셋팅 
        	  		    paramMap.put("dzCode","10300"); 	 //보통 예금 
        	  		 	int etr_amt =Integer.parseInt(paramMap.get("etrdpsAmt"))*-1;  //string  int parse 양수전환
	    	  	    	paramMap.put("etrdpsAmt", Integer.toString(etr_amt));   
	    	  	    	
    	  		    }else {  //일반 적인 데이터 
    	  		    	
        	  		    paramMap.put("drcrFg", "4");		 //대변 셋팅 
        	  		    String dztemp=  paramMap.get("dzCode2");  //대변 dzCode2 넣어줌 
        	  		    paramMap.put("dzCode",dztemp);
        	  		    
    	  		    }
    	  		    
    	  		    	
    	  	}
    	  	
    	   // System.out.println("MAP::::"+paramMap.get("inSq")+"   :    "+paramMap.get("etrdpsDt") );
    	    
    	    try {
    	    	
    	     dz01Mapper.dzInsert(paramMap);
    	    	
    	    }catch (Exception e) {
    	    	
    	    	e.printStackTrace();
			}
      }
 
      System.out.println("seq:::::::::::::"+etrdpsAmt+"::::::::::::::::::::::::::::::::"+tr_cd);
    
      
      return in_sq;
  }
    

   
    @Override
    public int dzDelete(Map<String, String> paramMap) { //더존 전표 삭제 
   	//전표 삭제 관련 
    try {
    		
        int chk = dz01Mapper.getDzCnt(paramMap);
		    	
	    	if( chk >0 ) {  //chk 가 0 보다 크면 발행된 전표가 있음 
	    		 return 0;
	    	}else {
	    		dz01Mapper.dzDelete(paramMap); // 0 보다 크지 않을 경우  발행된 전표가 없기 때문에 data 삭제 요청을 함 
	    	}
	  
    	}catch (Exception e) {
				e.printStackTrace();
				return 0;
    	}
	
      return 1;
   
    }
}