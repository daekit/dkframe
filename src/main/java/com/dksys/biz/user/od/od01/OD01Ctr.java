package com.dksys.biz.user.od.od01;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dksys.biz.exc.LogicException;
import com.dksys.biz.user.od.od01.service.OD01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od01")
public class OD01Ctr {
	
	private Logger logger = LoggerFactory.getLogger(OD01Ctr.class);
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD01Svc od01Svc;
	
    @PostMapping(value = "/selectOrdrgList")
	public String selectOrdrgList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od01Svc.selectOrdrgCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od01Svc.selectOrdrgList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectOrdrgDetailList")
	public String selectOrdrgDetailList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od01Svc.selectOrdrgDetailCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = od01Svc.selectOrdrgDetailList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectOrdrgInfo")
    public String selectOrdrgInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> result = od01Svc.selectOrdrgInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/getOrderInfo")
    public String getOrderInfo(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	Map<String, Object> result = od01Svc.getOrderInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectConfirmCount")
    public String selectConfirmCount(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int result = od01Svc.selectConfirmCount(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertOrdrg")
    public String insertOrdrg(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	od01Svc.insertOrdrg(paramMap, mRequest);
    	model.addAttribute("ordrgSeq", paramMap.get("ordrgSeq"));
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateOrdrg")
    public String updateOrdrg(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	od01Svc.updateOrdrg(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateSalesMng")
    public String updateSalesMng(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		od01Svc.updateSalesMng(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
			logger.error("==================== ERROR ====================", e);
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateOrdrgRmk")
    public String updateOrdrgRmk(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		od01Svc.updateOrdrgRmk(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
			logger.error("==================== ERROR ====================", e);
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateConfirm")
    public String updateConfirm(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		od01Svc.updateConfirm(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("confirm"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
			logger.error("==================== ERROR ====================", e);
		}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updateCancel")
	public String updateCancel(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		od01Svc.updateCancel(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("cancel"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
			logger.error("==================== ERROR ====================", e);
		}
    	return "jsonView";
		
		/*
		 * if(result == 0) { model.addAttribute("resultCode", 500);
		 * model.addAttribute("resultMessage", messageUtils.getMessage("bilgComplete"));
		 * } else if(result == 500) { model.addAttribute("resultCode", 500);
		 * model.addAttribute("resultMessage", messageUtils.getMessage("pchsClose")); }
		 * else if(result == 501) { model.addAttribute("resultCode", 500);
		 * model.addAttribute("resultMessage", messageUtils.getMessage("sellClose")); }
		 * else { model.addAttribute("resultCode", 200);
		 * model.addAttribute("resultMessage", messageUtils.getMessage("cancel")); }
		 * return "jsonView";
		 */
	}
    
    @DeleteMapping(value = "/deleteOrdrg")
    public String deleteOrdrg(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	od01Svc.deleteOrdrg(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
}