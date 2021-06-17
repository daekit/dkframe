package com.dksys.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dksys.biz.admin.cm.cm01.service.CM01Svc;
import com.dksys.biz.util.WebClientUtil;

@Controller
public class HomeController {

    @Autowired
    CM01Svc cm01Svc;
	
    @Autowired
    WebClientUtil webClientUtil;
    
    @GetMapping("/rest/get")
    public String get(Model model) {
    	String result = webClientUtil.get("http://asset2.dongkuk.com/cmn/getHeadInfo.json?orgCd=UNC50011431&comOrgCd=UNC");
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
	// 웰컴 페이지
    @GetMapping("/")
    public String welcome(Model model) {
    	return "redirect:/static/index.html";
    }
	
	// 권한 오류 시 요청처리
    @GetMapping("/noAuth")
    public String noAuth(Model model) {
    	throw new RuntimeException("토큰정보가 유효하지 않습니다.");
    }
    
    // 접근 가능한 메뉴정보
    @PostMapping("/selectMenuAuth")
    public String selectMenuAuth(@RequestBody Map<String, Object> param, Model model) {
    	String[] authArray = {"AUTH000"};
    	authArray = param.get("authInfo") != null ? param.get("authInfo").toString().split(",") : authArray;
    	List<Map<String, Object>> accessList = cm01Svc.selectMenuAuth(authArray);
    	model.addAttribute("accessList", accessList);
    	JSONArray jsonArray = new JSONArray();
    	
    	for (Map<String, Object> map : accessList) {
    		JSONObject json = new JSONObject();
    		for(Map.Entry<String, Object> entry : map.entrySet()) {
    			try {
    				if(entry.getKey().equals("menuUrl") || entry.getKey().equals("saveYn")) {
    	    			String key = entry.getKey();
    	    			String sValue = entry.getValue().toString();
    	    			if(key.equals("menuUrl")) {
	    	    			System.out.println("=============================");
	    	    			System.out.println(sValue.lastIndexOf("/"));
	    	    			System.out.println(sValue.lastIndexOf("."));
	    	    			System.out.println(sValue);
	    	    			if(sValue.lastIndexOf("/") > 0) {
	    	    				sValue = sValue.substring(sValue.lastIndexOf("/")+1, sValue.lastIndexOf("."));
	    	    			}
	    	    			System.out.println(sValue);
    	    			}
    	    			Object value = sValue;
    					json.put(key, value);
        			}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		jsonArray.put(json);
    	}
    	System.out.println(jsonArray.toString());
    	model.addAttribute("accessJSON", jsonArray.toString());
    	return "jsonView";
    }
    
    // 접근 가능한 메뉴정보
    @PostMapping("/selectSubMenuAuth")
    public String selectSubMenuAuth(@RequestBody Map<String, Object> param, Model model) {
    	String[] authArray = {"AUTH000"};
    	String upMenuId = "";
    	authArray = param.get("authInfo") != null ? param.get("authInfo").toString().split(",") : authArray;
    	upMenuId = param.get("upMenuId") != null ? param.get("upMenuId").toString() : "";
    	List<Map<String, Object>> accessSubList = cm01Svc.selectSubMenuAuth(authArray, upMenuId);
    	model.addAttribute("accessSubList", accessSubList);
    	return "jsonView";
    }
}