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
  	public void dzInsert(Map<String, String> paramMap){
	
	//---------------------------1차 INSERT--------------------------------------------------------------------------
	  
      String drcrFg = paramMap.get("drcrFg");
      String acctAm = paramMap.get("acctAm").replace(",", "");
      String acctCd = paramMap.get("acctCd");
      
      
      //금액이 -(음수)면 차,대변 바꿔서 put
      //drcrFg = 3 지급(차변)
      if("3".equals(drcrFg) && Integer.parseInt(acctAm) < 0){ /* drcrFg가 3이면서 음수면 4 */
          paramMap.put("drcrFg","4");
          //수금(대변)으로 바꾸고 금액에서 - 없앰
      }
      
      //drcrFg = 4 수금(대변)
      else if("4".equals(drcrFg) && Integer.parseInt(acctAm) < 0){ /* drcrFg가 4이면서 음수면 3 */
          paramMap.put("drcrFg","3");
          //지급(차변)으로 바꾸고 금액에서 - 없앰
      }
      
      //금액 양수로 바꾸고 put
      int acc = Integer.parseInt(acctAm);
      paramMap.put("acctAm", String.valueOf(acc));
      
      //지급어음 
      if("3".equals(drcrFg) && "0".equals(acctCd)) {
    	  paramMap.put("acctCd", "25200");
      }
      //받을어음
      else if("4".equals(drcrFg) && "0".equals(acctCd)) {
    	  paramMap.put("acctCd", "11000");
      }
      
      String billFg1 = (paramMap.get("bilTypCd")).substring(0,1);
      String billFg2 = (paramMap.get("bilTypCd")).substring(1,2);
      paramMap.put("billFg1", String.valueOf(billFg1));
      paramMap.put("billFg2", String.valueOf(billFg2));
      
      // dz01Mapper.dzInsert(paramMap);  잠시 주석처리. 테스트위해선 주석 풀어야함
      
      
      //--------------------------TRST_CERTI_NO 가 0이 아닌지 검사 / 0이 아니면 선수금--------------------------------------------------------------------------
      Map<String, String> checkTrstCertiNo = dz01Mapper.checkTrstCertiNo(paramMap);
      String trstCertiNo = checkTrstCertiNo.get("trstCertiNo");
      
      Map<String, String> acctInfo = dz01Mapper.getAcctAm(paramMap);
      String bilgAmt = acctInfo.get("bilgAmt");
      String bilgVatAmt = acctInfo.get("bilgVatAmt");
      String etrdpsAmt = acctInfo.get("etrdpsAmt");
      
      //첫번째 insert가 차변에 넣어주는 거였으면 대변에 넣어주고
      //대변에 넣어줬으면 차변에 넣어주기위해 바꿈
      if("3".equals(drcrFg)) {
    	  paramMap.put("drcrFg", "4");
      }
      
      else if("4".equals(drcrFg)) {
    	  paramMap.put("drcrFg", "3");
      }
      
      //나머지 값들은 넣어줄 필요가 없음 inSq로만(거래순번) 구분
      paramMap.put("acctCd", "");
      paramMap.put("billFg1", "");
      paramMap.put("billFg2", "");
      paramMap.put("billTypCd", "");
      paramMap.put("FrDt", "");
      paramMap.put("toDt", "");
      paramMap.put("ctNb", "");
      paramMap.put("isuNm", "");
      paramMap.put("", "");
      
      //선수금 없음
      if(trstCertiNo.equals(0))
      {
    	  //2번쨰 부가세 insert
    	  paramMap.put("lnSq", "2");
    	  paramMap.put("acctAm", bilgAmt);
    	  // dz01Mapper.dzInsert(paramMap);  2차 insert. 테스트위해선 주석 풀어야함
    	  
    	  //3번쨰 부가세 insert
    	  paramMap.put("lnSq", "3");
    	  paramMap.put("acctAm", bilgVatAmt);
    	  // dz01Mapper.dzInsert(paramMap);  3차 insert. 테스트위해선 주석 풀어야함
      }
      
      //선수금 있음
      else if(!trstCertiNo.equals(0))
      {
    	  //2번째 금액(선수금) insert
    	  paramMap.put("lnSq", "2");
    	  
    	  paramMap.put("acctAm", etrdpsAmt);
    	  // dz01Mapper.dzInsert(paramMap);  2차 insert. 테스트위해선 주석 풀어야함
      }
      
  }
/*	@Override
	public void testInsert() throws Exception{
		dz01Mapper.testInsert();
		cm06Svc.updateUserName();*/

}