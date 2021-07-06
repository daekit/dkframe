package com.dksys.biz.user.ar.ar13;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar13.service.AR13Svc;
import com.dksys.biz.user.od.od05.service.OD05Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar13")
public class AR13Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR13Svc ar13Svc;
	
    @PostMapping(value = "/selectPrjtMngTernKey")
	public String selectPrjtMngTernKey(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar13Svc.selectPrjtMngTernKeyCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar13Svc.selectPrjtMngTernKey(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}