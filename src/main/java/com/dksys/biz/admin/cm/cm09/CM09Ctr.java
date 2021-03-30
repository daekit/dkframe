package com.dksys.biz.admin.cm.cm09;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm09.service.CM09Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/cm/cm09")
public class CM09Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	CM09Svc cm09Svc;
	
    @PostMapping("/selectNotiList")
    public String selectNotiList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = cm09Svc.selectNotiCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> notiList = cm09Svc.selectNotiList(paramMap);
    	model.addAttribute("notiList", notiList);
    	return "jsonView";
    }
    
    @PostMapping("/selectNotiInfo")
    public String selectNotiInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> result = cm09Svc.selectNotiInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping("/selectNotiPopList")
    public String selectNotiPopList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<String> result = cm09Svc.selectNotiPopList();
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping("/insertNoti")
    public String insertNoti(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		cm09Svc.insertNoti(paramMap, mRequest);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
    @PutMapping("/updateNoti")
    public String updateNoti(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		cm09Svc.updateNoti(paramMap, mRequest);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
}