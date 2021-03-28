package com.dksys.biz.user.pp.pp04;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.pp.pp04.service.PP04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/pp/pp04")
public class PP04Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    PP04Svc pp04Svc;
	
   
    @PostMapping(value = "/selectMesMtrlRstlList")
	public String selectMesMtrlRstlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp04Svc.selectMesMtrlRstlCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesMtrlRstlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
      
    
    @PostMapping(value = "/selectMesAllocVehlDtlList")
	public String selectMesAllocVehlDtlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = pp04Svc.selectMesAllocVehlDtlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
 /*
    확정된 배차정보를 기준으로 출하요청서, 매출내역 생성   
  */
    @PostMapping(value = "/insertMesShipLisr")
    public String insertMesShip(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
	    	pp04Svc.insertMesShipList(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    
    
    
    /*  
     *   
     *     @PostMapping(value = "/copyInsert")
    public String copyInsert(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		pp04Svc.deleteCopy(param);
	    	pp04Svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    
    @PostMapping(value = "/selectPrdtAcinsInfo")
    public String selectPrdtAcinsInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = pp04Svc.selectPrdtAcinsInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
     @PostMapping(value = "/selectMesShipList")
	public String selectMesShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp04Svc.selectMesShipCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesShipList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}


    @PutMapping(value = "/updatMesSHip")
    public String updatMesSHip(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 pp04Svc.updatMesSHip(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @DeleteMapping(value = "/deleteMesShip")
    public String deleteMesShip(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	pp04Svc.deleteMesShip(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updatMesSHipConfirm")
    public String updatMesSHipConfirm(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 pp04Svc.updatMesSHipConfirm(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @PostMapping(value = "/selectMesAllocVehlList")
	public String selectMesAllocVehlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = pp04Svc.selectMesAllocVehlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
   */ 
    
}