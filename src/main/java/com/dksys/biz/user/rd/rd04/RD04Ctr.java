package com.dksys.biz.user.rd.rd04;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.rd.rd04.service.RD04Svc;

@Controller
@RequestMapping("/user/rd/rd04")
public class RD04Ctr {
    
    @Autowired
    RD04Svc rd04Svc;
	
    @PostMapping(value = "/selectRcvpayList")
	public String selectRcvpayList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = rd04Svc.selectRcvpayCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = rd04Svc.selectRcvpayList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}