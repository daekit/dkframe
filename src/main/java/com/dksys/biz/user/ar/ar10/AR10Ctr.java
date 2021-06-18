package com.dksys.biz.user.ar.ar10;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar10.service.AR10Svc;

@Controller
@RequestMapping("/user/ar/ar10")
public class AR10Ctr {
 
    @Autowired
    AR10Svc ar10Svc;
	
    @PostMapping(value = "/selectPchsSellList")
	public String selectPchsSellList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar10Svc.selectPchsSellCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, Object>> resultList = ar10Svc.selectPchsSellList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectPchsSellSumList")
	public String selectPchsSellSumList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar10Svc.selectPchsSellSumCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, Object>> resultList = ar10Svc.selectPchsSellSumList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
}