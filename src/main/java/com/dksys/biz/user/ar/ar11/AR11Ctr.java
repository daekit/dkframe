package com.dksys.biz.user.ar.ar11;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar11.service.AR11Svc;

@Controller
@RequestMapping("/user/ar/ar11")
public class AR11Ctr {
 
    @Autowired
    AR11Svc ar11Svc;
	
    @PostMapping(value = "/EtrdpsPayList")
	public String EtrdpsPayList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar11Svc.EtrdpsPayCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, Object>> resultList = ar11Svc.EtrdpsPayList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    

}