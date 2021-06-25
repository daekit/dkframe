package com.dksys.biz.user.sd.sd04;

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
import com.dksys.biz.user.sd.sd04.service.SD04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd04")
public class SD04Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SD04Svc sd04Svc;
	
    @PostMapping(value = "/selectOrderList")
	public String selectOrderList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = sd04Svc.selectOrderCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, Object>> orderList = sd04Svc.selectOrderList(paramMap);
    	model.addAttribute("orderList", orderList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectOrderInfo")
    public String selectOrderInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> orderInfo = sd04Svc.selectOrderInfo(paramMap);
    	model.addAttribute("orderInfo", orderInfo);
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectFixedOrderCount")
    public String selectFixedOrderCount(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int fixedOrderCount = sd04Svc.selectFixedOrderCount(paramMap);
    	model.addAttribute("fixedOrderCount", fixedOrderCount);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertOrder")
    public String insertOrder(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		sd04Svc.insertOrder(paramMap, mRequest);
    		model.addAttribute("odrSeq", paramMap.get("odrSeq"));
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateOrder")
    public String updateOrder(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		sd04Svc.updateOrder(paramMap, mRequest);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @DeleteMapping(value = "/deleteOrder")
    public String deleteOrder(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd04Svc.deleteOrder(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/closeOrder")
    public String closeOrder(@RequestBody List<Map<String, String>> paramMapList, ModelMap model) {
    	try {
    		sd04Svc.closeOrder(paramMapList);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("close"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
}