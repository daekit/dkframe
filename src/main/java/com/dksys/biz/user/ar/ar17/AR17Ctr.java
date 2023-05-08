package com.dksys.biz.user.ar.ar17;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar17.service.AR17Svc;

@Controller
@RequestMapping("/user/ar/ar17")
public class AR17Ctr {

	@Autowired
	AR17Svc ar17Svc;
	
    @PostMapping(value = "/selectTrmendAmtList")
	public String selectTrmendAmtList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar17Svc.selectTrmendAmtListCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar17Svc.selectTrmendAmtList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}
