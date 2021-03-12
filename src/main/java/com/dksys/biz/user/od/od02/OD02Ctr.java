package com.dksys.biz.user.od.od02;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.od.od02.service.OD02Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od02")
public class OD02Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD02Svc od02Svc;
	
    @PostMapping(value = "/selectPurchaseList")
	public String selectShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od02Svc.selectPurchaseCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od02Svc.selectPurchaseList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
}