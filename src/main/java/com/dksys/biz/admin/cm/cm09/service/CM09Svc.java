package com.dksys.biz.admin.cm.cm09.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface CM09Svc {

	public int insertNoti(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	public List<Map<String, String>> selectNotiList(Map<String, String> paramMap);

	public int selectNotiCount(Map<String, String> paramMap);

	public Map<String, Object> selectNotiInfo(Map<String, String> paramMap);

	public int updateNoti(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	public List<String> selectNotiPopList();

}