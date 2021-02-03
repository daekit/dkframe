package com.dksys.biz.admin.bm.bm02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.admin.bm.bm02.service.BM02Svc;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;

@Service
@Transactional("erpTransactionManager")
public class BM02SvcImpl implements BM02Svc {
	
    @Autowired
    BM02Mapper bm01Mapper;
    
    @Autowired
    CM08Svc cm08Svc;

	@Override
	public void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		// CREAT_PGM 결정되면 값 insert 필요!
		bm01Mapper.insertClnt(paramMap);
		cm08Svc.uploadFile(paramMap.get("clntCd"), mRequest);
	}

	@Override
	public int selectClntCount(Map<String, String> param) {
		return bm01Mapper.selectClntCount(param);
	}

	@Override
	public List<Map<String, String>> selectClntList(Map<String, String> param) {
		return bm01Mapper.selectClntList(param);
	}

	@Override
	public Map<String, String> selectClntInfo(Map<String, String> param) {
		return bm01Mapper.selectClntInfo(param);
	}
	
}