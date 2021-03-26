package com.dksys.biz.user.sd.sd08;

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
import com.dksys.biz.user.sd.sd08.service.SD08Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd08")
public class SD08Ctr {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD08Svc sd08Svc;
	
    @PostMapping(value = "/selectCplrUntpcList")
	public String selectCplrUntpcList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = sd08Svc.selectCplrUntpcCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = sd08Svc.selectCplrUntpcList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectCplrUntpc")
	public String selectCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {    	
    	Map<String, String> result = sd08Svc.selectCplrUntpc(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
	}
    
    @PostMapping(value = "/insertCplrUntpc")
    public String insertCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		int cnt = sd08Svc.selectCplrUntpcKey(paramMap);
    		if(cnt > 0) {
    			model.addAttribute("resultCode", 999);
            	model.addAttribute("resultMessage", "데이터가 중복 되었습니다.");
    		} else {
    			sd08Svc.insertCplrUntpc(paramMap);
            	model.addAttribute("resultCode", 200);
            	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));	
    		}
    	}catch (Exception e){
    		System.out.println(e.getMessage());
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/updateCplrUntpc")
    public String updateCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd08Svc.updateCplrUntpc(paramMap);

        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/deleteCplrUntpc")
    public String deleteCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd08Svc.deleteCplrUntpc(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
}