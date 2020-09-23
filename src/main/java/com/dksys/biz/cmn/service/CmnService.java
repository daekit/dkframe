package com.dksys.biz.cmn.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CmnService {

	List<Map<String, String>> uploadExcelFile(MultipartFile excelFile);

}