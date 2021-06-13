package com.dksys.biz.user.rd.rd05;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.rd.rd05.service.RD05Svc;

@Controller
@RequestMapping("/user/rd/rd05")
public class RD05Ctr {
    
    @Autowired
    RD05Svc rd05Svc;
	
    @PostMapping(value = "/selectRcvpayDailyList")
	public String selectRcvpayDailyList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = rd05Svc.selectRcvpayDailyCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = rd05Svc.selectRcvpayDailyList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectRcvpayDailyDtlList")
	public String selectRcvpayDailyDtlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = rd05Svc.selectRcvpayDailyDtlCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	System.out.println(paramMap);
    	List<Map<String, String>> resultList = rd05Svc.selectRcvpayDailyDtlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}