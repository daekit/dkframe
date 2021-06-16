package com.dksys.biz.user.od.od05;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.od.od05.service.OD05Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od05")
public class OD05Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD05Svc od05Svc;
	
    @PostMapping(value = "/selectSellSaleList")
	public String selectSellSaleList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od05Svc.selectSellSaleCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od05Svc.selectSellSaleList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}