package com.dksys.biz.user.od.od01;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.od.od01.service.OD01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/od/od01")
public class OD01Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    OD01Svc od01Svc;
	
    @PostMapping(value = "/selectOrderList")
	public String selectOrderList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = od01Svc.selectOrderCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> notiList = od01Svc.selectOrderList(paramMap);
    	model.addAttribute("notiList", notiList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/insertOrder")
    public String insertOrder(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	od01Svc.insertOrder(paramMap, mRequest);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
}