package com.dksys.biz.user.ar.ar12;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar12.service.AR12Svc;

@Controller
@RequestMapping("/user/ar/ar12")
public class AR12Ctr {
 
    @Autowired
    AR12Svc ar12Svc;
	
    @PostMapping(value = "/SelectPtnCreditList")
	public String SelectPtnCreditList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar12Svc.SelectPtnCreditCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, Object>> resultList = ar12Svc.SelectPtnCreditList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    

}