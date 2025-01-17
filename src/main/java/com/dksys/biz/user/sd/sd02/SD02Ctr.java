package com.dksys.biz.user.sd.sd02;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sd.sd02.service.SD02Svc;
import com.dksys.biz.util.MessageUtils;


@Controller
@RequestMapping("/user/sd/sd02")
public class SD02Ctr {
 
	//단가 관리 컨트롤러 UNIT PRICE -> UPR 표기
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD02Svc sd02svc;
    
    @PostMapping(value = "/selectSellList")
    public String selectSellList(@RequestBody Map<String, String> param, ModelMap model) {
 /*   	int totalCnt = sd02svc.selectSellCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
  */  
    	List<Map<String, String>> sellList = sd02svc.selectSellList(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }
    
    @PostMapping(value = "/selectPlanInfo")
    public String selectPlanInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = sd02svc.selectPlanInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertPlan")
    public String insertPlan(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		int checkPlanCount = sd02svc.checkPlan(param);
    		
    		if(checkPlanCount == 0) {
    			sd02svc.insertPlan(param);
    	    	model.addAttribute("resultCode", 200);
    	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    		} else {
    			model.addAttribute("resultCode", 500);
    	    	model.addAttribute("resultMessage", messageUtils.getMessage("alreadyPlan"));
    		}
    		
	    	
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @PutMapping(value = "/updatePlan")
    public String updatePlan(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 sd02svc.updatePlan(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @PostMapping(value = "/copyInsert")
    public String copyInsert(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    	    List<Map<String, Object>> selectCopy = sd02svc.selectCopy(param);
    	    String clntCdList = "";
    	    if(selectCopy != null) {
    	    	for(int i = 0; i < selectCopy.size(); i++) {
    	    		clntCdList += "'" + selectCopy.get(i).get("CLNT_CD") + "'";
    				
    				if(i != selectCopy.size()-1) {
    					clntCdList += ",";
    				}
    	    	}
    	    }
    	    
    	    param.put("clntCdList", clntCdList);
    		sd02svc.deleteCopy(param);
	    	sd02svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @PostMapping(value = "/copySelect")
    public String copySelect(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    	    List<Map<String, Object>> selectCopy = sd02svc.selectCopy(param);
    	    int size = 0;
    	    String clntNm = "";
    	    if(selectCopy.size() != 0) {
    	    	size = selectCopy.size();
    	    	clntNm = MapUtils.getString(selectCopy.get(0), "CLNT_NM");
    	    	if(size != 1) {
    	    		int otherSize = size -1;
    	    		clntNm = clntNm + "외 " + otherSize + "건";
    	    	}
    	    }
    	    model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultSize", size);
	    	model.addAttribute("resultClntNm", clntNm);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("select"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @DeleteMapping(value = "/deletePlan")
    public String deleteEst(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	sd02svc.deletePlan(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectSellDailyRep")
    public String selectSellDailyRep(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> sellList = sd02svc.selectSellDailyRep(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }

    @PostMapping(value = "/selectSellListInd")
    public String selectSellListInv(@RequestBody Map<String, String> param, ModelMap model) {

    	List<Map<String, String>> sellList = sd02svc.selectSellListInd(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }
    
    @PostMapping(value = "/copyPlanToNextMonth")
    public String copyPlanToNextMonth(@RequestBody Map<String, Object> param, ModelMap model) {
    	try {
    		int result = sd02svc.copyPlanToNextMonth(param);
    		if(result == 501) {
        		model.addAttribute("resultCode", 500);
        		model.addAttribute("resultMessage", "중복된 거래처가 있습니다.");
    		} else {
    			model.addAttribute("resultCode", 200);
        		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));	
    		}
    		
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
}