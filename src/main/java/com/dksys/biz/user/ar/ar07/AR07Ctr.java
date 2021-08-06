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
    		// 전전월 마감이 있으면 당월 매출만 계산 아니면, 전월 매출도 같이 계산
    		int closePreCnt = ar07Svc.selectPreMtCloseChkCount(paramMap);
        	if (closePreCnt > 0 ) {
        		int totalCnt = ar07Svc.selectMtClosCditCount(paramMap);
        		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
        		model.addAttribute("paginationInfo", paginationInfo);
        		
        		resultList = ar07Svc.selectMtClosCditList(paramMap);        		
        		
        	}else {
        		
        		int totalCnt = ar07Svc.selectPreMtClosCditCount(paramMap);
        		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
        		model.addAttribute("paginationInfo", paginationInfo);
        		
        		resultList = ar07Svc.selectPreMtClosCditList(paramMap);        
        	}
    		
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
		
		int closeCnt = ar07Svc.selectMtCloseChkCount(paramMap);
    	
		//  당월 마감 자료가 없으면 실적자료를 가져온다.
		
		paramMap.put("curDataYn", "N");
		if(closeCnt ==  0) {
			paramMap.put("curDataYn", "Y");
		}	
		
		List<Map<String, String>> resultList = ar07Svc.selectClosCditList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
	
    @PostMapping(value = "/selectEtrdpsSellList")
	public String selectEtrdpsSellList(@RequestBody Map<String, String> paramMap, ModelMap model) {

    	List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
    	
    	resultList = ar07Svc.selectEtrdpsSellList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    
    
    
}