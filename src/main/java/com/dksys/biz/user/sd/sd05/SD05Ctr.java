package com.dksys.biz.user.sd.sd05;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@PutMapping(value = "/insertProject")
	public String insertProject(@RequestBody Map<String, String> param, ModelMap model) {
		sd05Svc.insertProject(param);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		return "jsonView";
	}
}