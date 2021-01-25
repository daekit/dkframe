package com.dksys.biz.admin.bm.bm01.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.admin.bm.bm01.service.BM01Svc;

@Service
@Transactional("erpTransactionManager")
public class BM01SvcImpl implements BM01Svc {
	
    @Autowired
    BM01Mapper bm01Mapper;

	
}