package com.dksys.biz.user.od.od01;

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
import com.dksys.biz.user.od.od01.service.OD01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od01")
public class OD01Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD01Svc od01Svc;
	
    @PostMapping(value = "/selectOrderList")
	public String selectOrderList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od01Svc.selectOrderCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od01Svc.selectOrderList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectOrderDetailList")
	public String selectOrderDetailList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od01Svc.selectOrderDetailCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od01Svc.selectOrderDetailList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectOrderInfo")
    public String selectOrderInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> result = od01Svc.selectOrderInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/getOrderInfo")
    public String getOrderInfo(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	System.out.println("ctr");
    	System.out.println(paramMap);
    	Map<String, Object> result = od01Svc.getOrderInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertOrder")
    public String insertOrder(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	od01Svc.insertOrder(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateOrder")
    public String updateOrder(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	od01Svc.updateOrder(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateConfirm")
    public String updateConfirm(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int result = od01Svc.updateConfirm(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exceedLoan"));
		} else if(result == 500) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("pchsClose"));
		} else if(result == 501) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("sellClose"));
		} else {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("confirm"));
		}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateCancel")
	public String updateCancel(@RequestParam Map<String, String> paramMap, ModelMap model) {
		int result = od01Svc.updateCancel(paramMap);
		if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("bilgComplete"));
		} else if(result == 500) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("pchsClose"));
		} else if(result == 501) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("sellClose"));
		} else {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("cancel"));
		}
		return "jsonView";
	}
    
    @DeleteMapping(value = "/deleteOrder")
    public String deleteOrder(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	od01Svc.deleteOrder(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectConfirmCount")
    public String selectConfirmCount(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int result = od01Svc.selectConfirmCount(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
}