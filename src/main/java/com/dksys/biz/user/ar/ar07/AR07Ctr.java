package com.dksys.biz.user.ar.ar07;

import java.util.ArrayList;
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
    	

    	int closeCnt = ar07Svc.selectMtCloseChkCount(paramMap);
    	List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
    	// 월마감 이력이 있으면 AR07에서만 가져오고. 아니면 AR02를 동시에 가져온다.
    	if(closeCnt > 0 ) {
        	int totalCnt = ar07Svc.selectMtClosCditPreCount(paramMap);
    		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
        	model.addAttribute("paginationInfo", paginationInfo);
    		resultList = ar07Svc.selectMtCloseCditPreList(paramMap);  
        	
    	}else {
        	
        	int totalCnt = ar07Svc.selectMtClosCditCount(paramMap);
    		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
        	model.addAttribute("paginationInfo", paginationInfo);
        	
        	resultList = ar07Svc.selectMtClosCditList(paramMap);        		
    	}
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    private List<Map<String, String>> selectMtClosCditPreList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostMapping(value = "/selectClosCditList")
	public String selectClosCditList(@RequestBody Map<String, String> paramMap, ModelMap model) {

    	List<Map<String, String>> resultList = ar07Svc.selectClosCditList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
}