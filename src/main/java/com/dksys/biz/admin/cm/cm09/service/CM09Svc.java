package com.dksys.biz.admin.cm.cm09.service;

import java.util.List;
import java.util.Map;

public interface CM09Svc {

	public int insertNoti(Map<String, String> paramMap);

	public List<Map<String, String>> selectNotiList(Map<String, String> paramMap);

	public int selectNotiCount(Map<String, String> paramMap);

}