package com.dksys.biz.user.sm.sm10.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm10.mapper.SM10Mapper;
import com.dksys.biz.user.sm.sm10.service.SM10Svc;

@Service
@Transactional("erpTransactionManager")
public class SM10Svcmpl implements SM10Svc {
	
    @Autowired
    SM10Mapper sm10Mapper;

	@Override
	public int reCalcStock(Map<String, String> paramMap) {
	
		
//		<!--  현재고 반영 순서 -->
//		<!--  1. 현재고를 TEMP TABLE에 입력한다 -->
//		<!--  2. 기존 재고 수량/중랴을 지운다  -->
//		<!--  3. 2개의 테이블을 MERAGE 한다. -->
		
		 sm10Mapper.reCalcStockTemp(paramMap);
		 sm10Mapper.reCalcStockUpdate(paramMap);
		 sm10Mapper.reCalcStockMerge(paramMap);
	  	 sm10Mapper.reCalcStockDeleteTemp(paramMap);
		return 0;
	}

}