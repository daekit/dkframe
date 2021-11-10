package com.dksys.biz.douzone.dz.dz01.service.impl;

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

//	@Override
//	public void testInsert(Map<String, String> paramMap){
//
//		dz01Mapper.testInsert(paramMap);
//	}
  @Override
  	public void testInsert(Map<String, String> paramMap){
	
	  //-금액 처리
      String drcrFg = paramMap.get("drcrFg");
      String acctAm = paramMap.get("acctAm");
      
      //금액이 - 면 차,대변 바꿔서 put
      //drcrFg = 3 지급(차변)
      if("3".equals(drcrFg)&& Integer.parseInt(acctAm) < 0){ /* drcrFg가 3이면서 음수면 4 */
          paramMap.put("drcrFg","4");
          //수금(대변)으로 바꾸고 금액에서 - 없앰
          
          int acc = Integer.parseInt(acctAm);
          paramMap.put("acctAm", String.valueOf(acc));
      }
      //drcrFg = 4 수금(대변)
      else if("4".equals(drcrFg)&& Integer.parseInt(acctAm) < 0){ /* drcrFg가 4이면서 음수면 3 */
          paramMap.put("drcrFg","3");
          //지급(차변)으로 바꾸고 금액에서 - 없앰
          
          int acc = Integer.parseInt(acctAm);         
          paramMap.put("acctAm", String.valueOf(acc));
      }
      dz01Mapper.testInsert(paramMap);
  }
/*	@Override
	public void testInsert() throws Exception{
		dz01Mapper.testInsert();
		cm06Svc.updateUserName();*/
	
}