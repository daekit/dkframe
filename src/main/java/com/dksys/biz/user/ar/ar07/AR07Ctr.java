package com.dksys.biz.user.ar.ar07;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar07.service.AR07Svc;

@Controller
@RequestMapping("/user/ar/ar07")
public class AR07Ctr {
    
    @Autowired
    AR07Svc ar07Svc;
	
    @PostMapping(value = "/selectMtClosCditList")
	public String selectRcvpayList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar07Svc.selectMtClosCditCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar07Svc.selectMtClosCditList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}