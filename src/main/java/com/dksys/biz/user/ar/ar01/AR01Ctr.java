package com.dksys.biz.user.ar.ar01;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar01")
public class AR01Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR01Svc ar01Svc;
	
    @PostMapping(value = "/selectShipList")
	public String selectShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar01Svc.selectShipCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar01Svc.selectShipList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectShipInfo")
    public String selectShipInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> result = ar01Svc.selectShipInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/getOrderInfo")
    public String getOrderInfo(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	Map<String, Object> result = ar01Svc.getOrderInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
	@PostMapping(value = "/insertShip")
    public String insertShip(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		ar01Svc.insertShip(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
	
	@DeleteMapping(value = "/deleteShip")
    public String deleteShip(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	ar01Svc.deleteShip(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateShip")
    public String updateShip(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		ar01Svc.updateShip(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateConfirm")
    public String updateConfirm(@RequestParam Map<String, String> paramMap, ModelMap model) {
		ar01Svc.updateConfirm(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("confirm"));
    	return "jsonView";
    }
	
	@PostMapping(value = "/selectConfirmCount")
	public String selectConfirmCount(@RequestBody Map<String, String> paramMap, ModelMap model) {
		int result = ar01Svc.selectConfirmCount(paramMap);
		model.addAttribute("result", result);
		return "jsonView";
	}
	
}