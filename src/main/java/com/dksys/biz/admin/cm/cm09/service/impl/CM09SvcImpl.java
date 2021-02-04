package com.dksys.biz.admin.cm.cm09.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.admin.cm.cm09.mapper.CM09Mapper;
import com.dksys.biz.admin.cm.cm09.service.CM09Svc;
import com.google.gson.Gson;

@Service
public class CM09SvcImpl implements CM09Svc {
	
    @Autowired
    CM09Mapper cm09Mapper;
    
    @Autowired
    CM08Svc cm08Svc;

	@Override
	public List<Map<String, String>> selectNotiList(Map<String, String> paramMap) {
		return cm09Mapper.selectNotiList(paramMap);
	}

	@Override
	public int selectNotiCount(Map<String, String> paramMap) {
		return cm09Mapper.selectNotiCount(paramMap);
	}

	@Override
	public int insertNoti(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = cm09Mapper.insertNoti(paramMap);
		cm08Svc.uploadFile("TB_CM09M01", paramMap.get("notiKey"), mRequest);
		return result;
	}

	@Override
	public Map<String, Object> selectNotiInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("notiKey")));
		returnMap.put("notiInfo", cm09Mapper.selectNotiInfo(paramMap));
		return returnMap; 
	}

	@Override
	public int updateNoti(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = cm09Mapper.updateNoti(paramMap);
		cm08Svc.uploadFile("TB_CM09M01", paramMap.get("notiKey"), mRequest);
		Gson gson = new Gson();
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		return result;
	}

}