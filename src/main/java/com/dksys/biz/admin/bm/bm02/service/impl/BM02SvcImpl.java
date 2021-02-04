package com.dksys.biz.admin.bm.bm02.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm02.mapper.BM02Mapper;
import com.dksys.biz.admin.bm.bm02.service.BM02Svc;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.google.gson.Gson;

@Service
@Transactional("erpTransactionManager")
public class BM02SvcImpl implements BM02Svc {
	
    @Autowired
    BM02Mapper bm01Mapper;
    
    @Autowired
    CM08Svc cm08Svc;

	@Override
	public void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		bm01Mapper.insertClnt(paramMap);
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd"), mRequest);
	}
	
	@Override
	public void updateClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		bm01Mapper.updateClnt(paramMap);
		// 파일 업로드
		cm08Svc.uploadFile("TB_BM02M01", paramMap.get("clntCd"), mRequest);
		// 파일 삭제
		Gson gson = new Gson();
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			// 파일 삭제 서비스 호출(파일키만 넘겨주고 키로 정보를 얻어온 다음 물리파일 삭제후 디비에서도 삭제해야함)
		}
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