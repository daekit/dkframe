package com.dksys.biz.user.ar.ar06;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar06.service.AR06Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar06")
public class AR06Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR06Svc ar06Svc;
	
    @PostMapping(value = "/selectTaxInvoiceList")
	public String selectTaxInvoiceList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar06Svc.selectTaxInvoiceCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar06Svc.selectTaxInvoiceList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectTaxInvoiceInfo")
    public String selectTaxInvoiceInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = ar06Svc.selectTaxInvoiceInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertTaxInvoice")
    public String insertPrdtAcins(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		ar06Svc.insertTaxInvoice(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @PutMapping(value = "/updateTaxInvoice")
    public String updatePrdtAcins(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		ar06Svc.updateTaxInvoice(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @DeleteMapping(value = "/deleteTaxInvoice")
    public String deletePrdtAcins(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	ar06Svc.deleteTaxInvoice(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }

}