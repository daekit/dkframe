package com.dksys.biz.user.sd.sd03;

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
import com.dksys.biz.user.sd.sd03.service.SD03Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd03")
public class SD03Ctr {
 
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD03Svc sd03svc;
    
    @PostMapping("/selectEstList")
    public String selectEstList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd03svc.selectEstCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, Object>> estList = sd03svc.selectEstList(param);
    	model.addAttribute("estList", estList);
        return "jsonView";
    }
    
    @PostMapping(value = "/selectEstInfo")
    public String selectEstInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> estInfo = sd03svc.selectEstInfo(paramMap);
    	model.addAttribute("estInfo", estInfo);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertEst")
	public String insertEst(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
	    	sd03svc.insertEst(paramMap, mRequest);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
		return "jsonView";
	}
	
	@PutMapping(value = "/updateEst")
	public String updateEst(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		try {
			sd03svc.updateEst(paramMap, mRequest);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@DeleteMapping(value = "/deleteEst")
    public String deleteEst(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	sd03svc.deleteEst(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
}