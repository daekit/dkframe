package com.dksys.biz.user.fi.fi01;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.fi.fi01.service.FI01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/fi/fi01")
public class FI01Ctr {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    FI01Svc fi01Svc;
	
    @PostMapping(value = "/selectPrftDeptList")
	public String selectPrftDeptList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = fi01Svc.selectPrftDeptCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = fi01Svc.selectPrftDeptList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectUpperDept")
	public String selectUpperDept(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> upperDeptList = fi01Svc.selectUpperDept(paramMap);
    	List<Map<String, String>> lastDeptList = fi01Svc.selecrLastDept(paramMap);
    	model.addAttribute("upperDeptList", upperDeptList);
    	model.addAttribute("lastDeptList", lastDeptList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectPrftDeptDetail")
	public String selectPrftDeptDetail(@RequestBody Map<String, String> paramMap, ModelMap model) {    	
    	Map<String, String> result = fi01Svc.selectPrftDeptDetail(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
	}
    
    @PostMapping(value = "/insertPrftDept")
    public String insertPrftDept(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		int duplicate = fi01Svc.selectPrdtDeptDuplicate(paramMap);
    		if(duplicate > 0 ) {
            	model.addAttribute("resultCode", 999);
            	model.addAttribute("resultMessage", "중복 에러입니다. 키 값을 확인해주세요.");
    		} else {
    			fi01Svc.insertPrftDept(paramMap);
            	model.addAttribute("resultCode", 200);
            	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    		}
    		
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/updatePrftDept")
    public String updateCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi01Svc.updatePrftDept(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/deletePrftDept")
    public String deleteCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi01Svc.deletePrftDept(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }    
}