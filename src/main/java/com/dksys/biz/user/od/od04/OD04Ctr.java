package com.dksys.biz.user.od.od04;

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
import com.dksys.biz.user.od.od04.service.OD04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od04")
public class OD04Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD04Svc od04Svc;
	
	@PostMapping(value = "/selectReqList")
    public String selectReqList(@RequestBody Map<String, String> param, ModelMap model) {
		int reqCnt = od04Svc.selectReqListCount(param);
		PaginationInfo paginationInfo = new PaginationInfo(param, reqCnt);
		List<Map<String, String>> resultList = od04Svc.selectReqList(param);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
		model.addAttribute("resultList", resultList);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectReq")
    public String selectReqInfo(@RequestBody Map<String, String> param, ModelMap model) {
		Map<String, Object> result = od04Svc.selectReq(param);
		model.addAttribute("result", result);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectReqOrder") 
	public String selectReqOrder(@RequestBody Map<String, Object> param, ModelMap model) { 
		Map<String, Object> result = od04Svc.selectReqOrder(param);
		model.addAttribute("result", result);
		return "jsonView"; 
	}
    
	@PostMapping(value = "/insertReq")
    public String insertReq(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		od04Svc.insertReq(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
	
	@DeleteMapping(value = "/deleteReq")
    public String deleteReq(@RequestBody Map<String, String> paramMap, ModelMap model) {
		od04Svc.deleteReq(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateReq")
    public String updateReq(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		od04Svc.updateReq(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
	@PostMapping(value = "/updateRecpt")
    public String updateRecpt(@RequestBody Map<String, String> paramMap, ModelMap model) {
		od04Svc.updateRecpt(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
	@PostMapping(value = "/updateRecptList")
    public String updateRecptList(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		od04Svc.updateRecptList(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
}