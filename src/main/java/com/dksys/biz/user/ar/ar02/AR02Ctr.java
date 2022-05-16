package com.dksys.biz.user.ar.ar02;

import java.util.HashMap;
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

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.exc.LogicException;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar02")
public class AR02Ctr {
 
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    AR02Svc ar02Svc;

    @Autowired
    CM08Svc cm08Svc;
    
    @PostMapping(value = "/selectSellMainList")
	public String selectSellMainList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar02Svc.selectSellMainCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar02Svc.selectSellMainList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectSellList")
	public String selectSellList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar02Svc.selectSellCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar02Svc.selectSellList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectSellPchSumList")
	public String selectSellPchSumList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar02Svc.selectSellPchSumCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar02Svc.selectSellPchSumList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectSellSumList")
    public String selectSellSumList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = ar02Svc.selectSellSumList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectSellInfo")
    public String selectSellInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = ar02Svc.selectSellInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PutMapping(value = "/updatePchsSell")
    public String updatePchsSell(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	try {
    		ar02Svc.updatePchsSell(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
    
    @PutMapping(value = "/updatePchsSellPart")
    public String updatePchsSellPart(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		ar02Svc.updatePchsSellPart(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}

    	return "jsonView";
    }
	@DeleteMapping(value = "/deleteSell")
    public String deleteSell(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar02Svc.deleteSell(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }

	@PostMapping(value = "/insertPchsSell")
    public String insertPchsSell(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar02Svc.insertPchsSell(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@PostMapping(value = "/insertSalesDivision")
    public String insertSalesDivision(@RequestBody List<Map<String, String>> paramList, ModelMap model) {
		try {
			ar02Svc.insertSalesDivision(paramList);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("save"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateSalesClnt")
    public String updateSalesClnt(@RequestBody List<Map<String, String>> paramList, ModelMap model) {
		try {
			ar02Svc.updateSalesClnt(paramList);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@PostMapping(value = "/excelDownload")
	public String excelDownload(@RequestBody Map<String, String> paramMap, ModelMap model) {
		List<Map<String, String>> resultList = ar02Svc.selectSellList(paramMap);
		String fileName = cm08Svc.excelDownload(resultList, "AR0201M01.xlsx");
		model.addAttribute("fileName", fileName);
		return "jsonView";
	}

	@PostMapping(value = "/insertDeletePchsSell")
    public String insertDeletePchsSell(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar02Svc.insertDeletePchsSell(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}

    	return "jsonView";
    }
	
	@PostMapping(value = "/checkClose")
	public String checkClose(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar02Svc.checkClose(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}catch(LogicException le) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", le.getMessage());
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}

		System.out.println(model.getAttribute("resultCode"));
		System.out.println(model.getAttribute("resultMessage"));
		return "jsonView";
	}
}