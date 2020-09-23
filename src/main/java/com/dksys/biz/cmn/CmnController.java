package com.dksys.biz.cmn;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.cmn.service.CmnService;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/cmn")
public class CmnController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CmnService cmnService;
    
    // 엑셀업로드
    @PostMapping("/uploadExcelFile")
    public String selectAuthInfo(MultipartHttpServletRequest request, ModelMap model) {
    	MultipartFile file = null;
    	Iterator<String> iterator = request.getFileNames();
    	if(iterator.hasNext()) {
    		file = request.getFile(iterator.next());
    	}
    	List<Map<String, String>> list = cmnService.uploadExcelFile(file);
    	model.addAttribute("list", list);
        return "jsonView";
    }
    
}