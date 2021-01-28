package com.dksys.biz.admin.bm.bm02.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.admin.bm.bm02.service.BM02Svc;

@Service
@Transactional("erpTransactionManager")
public class BM02SvcImpl implements BM02Svc {
	
    @Autowired
    BM02Mapper bm01Mapper;

	
}