package com.dksys.biz.user.sd.sd05;

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
import com.dksys.biz.user.sd.sd05.service.SD05Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd05")
public class SD05Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SD05Svc sd05Svc;
	
	@PostMapping(value = "/selectProjectList")
    public String selectProjectList(@RequestBody Map<String, String> param, ModelMap model) {
		int totalCnt = sd05Svc.selectProjectCount(param);
		PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
		List<Map<String, String>> sd0501m01 = sd05Svc.selectProjectList(param);
		model.addAttribute("sd0501m01", sd0501m01);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectPrjInfo")
    public String selectOrderInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> result = sd05Svc.selectPrjInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }

	@PostMapping(value = "/selectMakerPchsClntCd")
    public String selectMakerPchsClntCd(@RequestBody Map<String, String> paramMap, ModelMap model) {
		 Map<String, String> pchsClnt = sd05Svc.selectMakerPchsClntCd(paramMap);
    	model.addAttribute("result", pchsClnt);
    	return "jsonView";
    }
	
	@PostMapping(value = "/selectProjectcoCdList")
    public String selectProjectcoCdList(@RequestBody Map<String, String> param, ModelMap model) {
		List<Map<String, String>> codecoCd = sd05Svc.selectProjectcoCdList(param);
		model.addAttribute("codecoCd", codecoCd);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectProjectNameList")
    public String selectProjectNameList(@RequestBody Map<String, String> param, ModelMap model) {
		List<Map<String, String>> codeName = sd05Svc.selectProjectNameList(param);
		model.addAttribute("codeName", codeName);
        return "jsonView";
    }
	
	@PostMapping("/selectProjectKeyList")
    public String selectProjectKeyList(@RequestBody Map<String, String> param, ModelMap model) {
		Map<String, String> codeName = sd05Svc.selectProjectKeyList(param);
		model.addAttribute("codeName", codeName);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectProjectClntList")
    public String selectProjectClntList(@RequestBody Map<String, String> param, ModelMap model) {
		List<Map<String, String>> sd0501p03 = sd05Svc.selectProjectClntList(param);
		model.addAttribute("sd0501p03", sd0501p03);
        return "jsonView";
    }
	
	@PostMapping(value = "/insertProject")
	public String insertProject(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		sd05Svc.insertProject(paramMap, mRequest);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		return "jsonView";
	}
	
	@PostMapping(value = "/updateProject")
	public String updateProject(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		sd05Svc.updateProject(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
	@PostMapping(value = "/selectConfirmCount")
	public String selectConfirmCount(@RequestBody Map<String, String> paramMap, ModelMap model) {
		int result = sd05Svc.selectConfirmCount(paramMap);
		model.addAttribute("result", result);
		return "jsonView";
	}
	
	@PutMapping(value = "/deleteProject")
	public String deleteProject(@RequestBody Map<String, String> param, ModelMap model) {
		sd05Svc.deleteProject(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
	
	@DeleteMapping(value = "/deleteProjectDtl")
	public String deleteProjectDtl(@RequestBody List<Map<String, String>> paramList, ModelMap model) {
		try {
			sd05Svc.deleteProjectDtl(paramList);
			model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));	
		}catch(Exception e){
			model.addAttribute("resultCode", 500);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}    	
    	return "jsonView";
    }
	
	@PostMapping(value = "/selectPrdtDivCd")
	public String selectPrdtDivCd(@RequestBody Map<String, String> paramMap, ModelMap model) {
		List<Map<String, String>> codeInfoList = sd05Svc.selectPrdtDivCd(paramMap);
		model.addAttribute("codeInfoList", codeInfoList);
		return "jsonView";
	}
	
	@PostMapping(value = "/prdtDivCombo")
	public String prdtDivCombo(@RequestBody Map<String, String> paramMap, ModelMap model) {
		List<Map<String, String>> codeInfoList = sd05Svc.prdtDivCombo(paramMap);
		model.addAttribute("codeInfoList", codeInfoList);
		return "jsonView";
	}
	
	@PostMapping(value = "/prdtSpecCombo")
	public String prdtSpecCombo(@RequestBody Map<String, String> paramMap, ModelMap model) {
		List<Map<String, String>> codeInfoList = sd05Svc.prdtSpecCombo(paramMap);
		int totalCnt = codeInfoList.size();
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("codeInfoList", codeInfoList);
		return "jsonView";
	}
	
	
	@PostMapping(value = "/selectChkOrdrgYn")
    public String selectChkOrdrgYn(@RequestBody Map<String, String> param, ModelMap model) {
		int result = sd05Svc.selectChkOrdrgYn(param);
		model.addAttribute("result", result);
        return "jsonView";
    }
}