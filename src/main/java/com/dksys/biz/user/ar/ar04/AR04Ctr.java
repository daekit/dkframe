package com.dksys.biz.user.ar.ar04;

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
import com.dksys.biz.user.ar.ar04.service.AR04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar04")
public class AR04Ctr {
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR04Svc ar04Svc;
	
	@PostMapping(value = "/insertBilg")
	public String insertBilg(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		try {
			ar04Svc.insertBilg(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("confirm"));
		}catch(Exception e){
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
		return "jsonView";
	}
	
    @PostMapping(value = "/selectTaxBilgList")
	public String selectTaxBilgList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar04Svc.selectTaxBilgCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar04Svc.selectTaxBilgList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectTaxBilgDetailList")
	public String selectTaxBilgDetailList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar04Svc.selectTaxBilgDetailCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar04Svc.selectTaxBilgDetailList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectTaxBilg")
	public String selectTaxBilg(@RequestBody Map<String, String> paramMap, ModelMap model) {

    	Map<String, String> result = ar04Svc.selectTaxBilg(paramMap);
    	List<Map<String, String>> dtlResult = ar04Svc.selectBilgDetailList(paramMap);
    	model.addAttribute("result", result);
    	model.addAttribute("dtlResult", dtlResult);

    	return "jsonView";
	}
    
    @PostMapping(value = "/updateTaxBilg")
    public String updateTaxBilg(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		ar04Svc.updateTaxBilg(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
    @PostMapping(value = "/updateBilg")
    public String updateBilg(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int result = ar04Svc.updateBilg(paramMap);
    	if(result == 0) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("noUpdateTax"));
    	} else {
    		model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectTaxRcvList")
	public String selectTaxRcvList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar04Svc.selectTaxRcvCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> taxRcvList = ar04Svc.selectTaxRcvList(paramMap);
    	model.addAttribute("taxRcvList", taxRcvList);
    	return "jsonView";
	}
	
	@PostMapping(value = "/insertTaxHdUpdate")
	public String insertTaxHdUpdate(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		ar04Svc.insertTaxHdUpdate(paramMap);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
		return "jsonView";
	}
	
	@PostMapping(value = "/insertTaxHd")
	public String insertTaxHd(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		ar04Svc.insertTaxHd(paramMap);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
		return "jsonView";
	}
	
	@PostMapping(value = "/insertTaxHdReSend")
	public String insertTaxHdReSend(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		ar04Svc.insertTaxHdReSend(paramMap);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
		return "jsonView";
	}
	
	@PostMapping(value = "/insertTaxHdCancel")
	public String insertTaxHdCancel(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		ar04Svc.insertTaxHdCancel(paramMap);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
		return "jsonView";
	}
	
	@PutMapping(value = "/updateBilgCancel")
    public String updateBilgCancel(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int result = ar04Svc.updateBilgCancel(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("taxComplete"));
		} else {
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}
    	return "jsonView";
    }
	
	@PostMapping(value = "/updateBilgRvrs")
    public String updateBilgRvrs(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int result = ar04Svc.updateBilgRvrs(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("taxComplete"));
		} else {
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}
    	return "jsonView";
    }

	
	@PostMapping(value = "/updateBilgRvrsCancel")
    public String updateBilgRvrsCancel(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int result = ar04Svc.updateBilgRvrsCancel(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("taxComplete"));
		} else {
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}
    	return "jsonView";
    }
	
	
	@PutMapping(value = "/updateNote")
    public String updateNote(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int result = ar04Svc.updateNote(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("taxComplete"));
		} else {
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}
    	return "jsonView";
    }
	
	@PostMapping(value = "/deleteTaxHd")
	public String deleteTaxHd(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		try {
			ar04Svc.deleteTaxHd(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		}catch(Exception e){
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
		return "jsonView";
	}

}