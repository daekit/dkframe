package com.dksys.biz.user.ar.ar16;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar16.service.AR16Svc;

@Controller
@RequestMapping("/user/ar/ar16")
public class AR16Ctr {

	@Autowired
	AR16Svc ar16Svc;
	
    @PostMapping(value = "/selectLoanAmt")
	public String selectLoanAmt(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar16Svc.selectLoanAmtCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar16Svc.selectLoanAmtList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}
