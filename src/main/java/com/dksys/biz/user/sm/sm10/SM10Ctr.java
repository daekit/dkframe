package com.dksys.biz.user.sm.sm10;

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
import com.dksys.biz.user.sm.sm10.service.SM10Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm10")
public class SM10Ctr {
 
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM10Svc sm10svc;
    
    
  //selectStockHistoryList 재고 변동 이력 리스트 조회 
    @PostMapping("/reCalcStock")
    public String reCalcStock(@RequestBody Map<String, String> param, ModelMap model) {
    	
    	int totalCnt = sm10svc.reCalcStock(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
        return "jsonView";
    }
      
}