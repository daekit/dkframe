package com.dksys.biz.user.ar.ar05;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar05.service.AR05Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar05")
public class AR05Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	AR05Svc ar05Svc;
	
	// 입금 리스트 조회
    @PostMapping("/selectEtrdpsList")
    public String selectEtrdpsList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar05Svc.selectEtrdpsCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> etrDpsList = ar05Svc.selectEtrdpsList(paramMap);
    	model.addAttribute("etrDpsList", etrDpsList);
        return "jsonView";
    }
    
    // 입금 정보 조회
    @PostMapping("/selectEtrdpsInfo")
    public String selectEtrdpsInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> etrdpsInfo = ar05Svc.selectEtrdpsInfo(paramMap);
    	model.addAttribute("etrdpsInfo", etrdpsInfo);
        return "jsonView";
    }
    
    // 입금-매출 정보 조회
    @PostMapping("/selectEtrdpsDtlList")
    public String selectEtrdpsDtlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = ar05Svc.selectEtrdpsDtlList(paramMap);
    	model.addAttribute("resultList", resultList);
        return "jsonView";
    }
    
    // 입금 등록
	@PostMapping("/insertEtrdps")
    public String insertEtrdps(@RequestBody Map<String, Object> paramMap, ModelMap model) {

    	try {
    		int result = ar05Svc.insertEtrdps(paramMap);
			if(result == 500){
				model.addAttribute("resultCode", 500);
		    	model.addAttribute("resultMessage", messageUtils.getMessage("etrdpsClose"));
			}else if(result == 300){
				model.addAttribute("resultCode", 300);
		    	model.addAttribute("resultMessage", messageUtils.getMessage("alreadyKey"));
			}else {
		    		
		    		model.addAttribute("resultCode", 200);
		    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		    }
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	// 입금 수정
	@PutMapping("/updateEtrdps")
    public String updateEtrdps(@RequestBody Map<String, Object> paramMap, ModelMap model) {

		try {
			int result =ar05Svc.updateEtrdps(paramMap);
			if(result == 500){
				model.addAttribute("resultCode", 500);
		    	model.addAttribute("resultMessage", messageUtils.getMessage("etrdpsClose"));
			}else {
		    		model.addAttribute("resultCode", 200);
		    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
			}
		}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	// 입금 삭제
	@DeleteMapping("/deleteEtrdps")
    public String deleteEtrdps(@RequestBody Map<String, String> paramMap, ModelMap model) {

		try {
			int result = ar05Svc.deleteEtrdps(paramMap);
			if(result == 500){
				model.addAttribute("resultCode", 500);
		    	model.addAttribute("resultMessage", messageUtils.getMessage("etrdpsClose"));
			}else {
		    		model.addAttribute("resultCode", 200);
		    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
	    	}
		}catch(Exception e){
	    		model.addAttribute("resultCode", 500);
	    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	// 입금 매칭 정보 삭제 
	
	@PutMapping("/calcelMatch")
    public String calcelMatch(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		try {
			Map<String, String> etrdpsData = (Map<String, String>) paramMap.get("etrdpsData");
			// etrdpsData.put("etrdpsSeq", MapUtils.getString(paramMap, "etrdpsSeq"));
			// etrdpsData.put("diffAmt", MapUtils.getString(paramMap, "diffAmt"));
			int result = ar05Svc.calcelMatch(etrdpsData);
			if(result == 500){
				model.addAttribute("resultCode", 500);
		    	model.addAttribute("resultMessage", messageUtils.getMessage("etrdpsClose"));
			}else {
		    		model.addAttribute("resultCode", 200);
		    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
			}
		}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }	
	
	// 입금 매칭정보
    @PostMapping("/selectEtrdpsMap")
    public String selectEtrdpsMap(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar05Svc.selectEtrdpsMapCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> etrDpsList = ar05Svc.selectEtrdpsMap(paramMap);
    	model.addAttribute("etrDpsList", etrDpsList);
        return "jsonView";
    }
    
    //더존 sndseq 값 업데이트  20220630 kdm 
    @PostMapping("/updateDzSndSeq")
    public String  updateDzSndSeq(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int chk = ar05Svc. updateDzSndSeq(paramMap);
      
    	if(chk == 0 ) {
	    	model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("false"));
    	}else {
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}
    	 
        return "jsonView";
    }
  
    
}