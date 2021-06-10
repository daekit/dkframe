package com.dksys.biz.user.ar.ar02;

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
    	int result = ar02Svc.updatePchsSell(paramMap);
    	if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exceedLoan"));
		} else if (result == 500){
			model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("pchsClose"));
		} else if (result == 501){
			model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("sellClose"));
		} else {
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}
    	return "jsonView";
    }

	@DeleteMapping(value = "/deleteSell")
    public String deleteSell(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar02Svc.deleteSell(paramMap);
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
	
}