package com.dksys.biz.user.pp.pp04;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
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
import com.dksys.biz.user.ar.ar01.service.AR01Svc;
import com.dksys.biz.user.pp.pp04.service.PP04Svc;
import com.dksys.biz.util.MessageUtils;

import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/user/pp/pp04")
public class PP04Ctr {
 
	@Autowired
	MessageUtils messageUtils;

    @Autowired
    PP04Svc pp04Svc;
    
    @Autowired
    AR01Svc ar01Svc;
	

    @PostMapping(value = "/selectMesMtrlRstlFirstList")
	public String selectMesMtrlRstlFirstList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp04Svc.selectMesMtrlRstlFirstCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesMtrlRstlFirstList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectMesMtrlRstlUnGroupList")
	public String selectMesMtrlRstlUnGroupList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp04Svc.selectMesMtrlRstlUnGroupListCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesMtrlRstlUnGroupList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectMesMtrlRstlList")
	public String selectMesMtrlRstlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	
		String loadOrgNo = "";
		String loadOrgNoGroup = MapUtils.getString(paramMap, "loadOrgNoGroup");
		
		String[] loadOrgNoList = loadOrgNoGroup.split(",");
		for(int i = 0; i < loadOrgNoList.length; i++) {
			loadOrgNo += "'" + loadOrgNoList[i] + "'";
			
			if(i != loadOrgNoList.length-1) {
				loadOrgNo += ",";
			}
		}
		
		paramMap.put("loadOrgNo", loadOrgNo);
    	
    	int totalCnt = pp04Svc.selectMesMtrlRstlCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesMtrlRstlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
      
    
    @PostMapping(value = "/selectMesAllocVehlDtlList")
	public String selectMesAllocVehlDtlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = pp04Svc.selectMesAllocVehlDtlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
 /*
    확정된 배차정보를 기준으로 출하요청서, 매출내역 생성   
  */
    @PostMapping(value = "/insertMesShipList")
    public String insertMesShipList(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	try {
    		pp04Svc.insertMesShipList(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch (Exception e) {
    		model.addAttribute("resultCode", 500);
        	model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
		
    	return "jsonView";
    }
    
 /*
    생성된 출하 요청서를 삭제
  */
    @PostMapping(value = "/deleteMesShipList")
    public String deleteMesShipList(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	int cnt = pp04Svc.selectBilgNoCnt(paramMap);
    	if(cnt > 0) {
        	model.addAttribute("resultCode", 999);
        	model.addAttribute("resultMessage", "청구 된 데이터가 있습니다.");
    	} else {
    		pp04Svc.deleteMesShipList(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}
		
    	return "jsonView";
    }
    
    
    /*  
     *   
     *     @PostMapping(value = "/copyInsert")
    public String copyInsert(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		pp04Svc.deleteCopy(param);
	    	pp04Svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    
    @PostMapping(value = "/selectPrdtAcinsInfo")
    public String selectPrdtAcinsInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = pp04Svc.selectPrdtAcinsInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
     @PostMapping(value = "/selectMesShipList")
	public String selectMesShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp04Svc.selectMesShipCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = pp04Svc.selectMesShipList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}


    @PutMapping(value = "/updatMesSHip")
    public String updatMesSHip(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 pp04Svc.updatMesSHip(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @DeleteMapping(value = "/deleteMesShip")
    public String deleteMesShip(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	pp04Svc.deleteMesShip(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    @PutMapping(value = "/updatMesSHipConfirm")
    public String updatMesSHipConfirm(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 pp04Svc.updatMesSHipConfirm(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @PostMapping(value = "/selectMesAllocVehlList")
	public String selectMesAllocVehlList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = pp04Svc.selectMesAllocVehlList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
   */ 
    
    @PostMapping(value = "/daliyAccessList")
	public String daliyAccessList(@RequestBody Map<String, String> paramMap, ModelMap model) throws IOException {
    	// int totalCnt = pp04Svc.selectMesMtrlRstlCount(paramMap);
		// PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	// model.addAttribute("paginationInfo", paginationInfo);
    	List<Map<String, String>> resultList = pp04Svc.daliyAccessList(paramMap);
    	
    	
    	
    	for(Map<String, String> result : resultList) {
    		String accessTime = MapUtils.getString(result, "firstAccessTime");
    		accessTime = accessTime.substring(0,4)+"-"+accessTime.substring(4,6)+"-"+accessTime.substring(6,8)+" "+accessTime.substring(8,10)+":"+accessTime.substring(10,12)+":"+accessTime.substring(12,14)+".000";
    		
    		String accessUser = MapUtils.getString(result, "userId");
    		
    		URL url = new URL("http://log.smart-factory.kr/apisvc/sendLogData.json");
            String postData = "foo1=bar1&foo2=bar2";
            JSONObject json = new JSONObject();
            json.put("crtfcKey", "$5$API$y3q3QB1tkSn00x7LNc4Cu9kdL.zboDeV4V8AwFFT/RA");
            json.put("logDt", "2022-09-28 14:22:31.958");
            json.put("useSe", " 접속");
            json.put("sysUser", "userId");
            json.put("conectIp", "0.0.0.0.0.1");
            json.put("dataUsgqty", "0");
     
            String jsonString = json.toString();
            System.out.println("============== 변환하기 전 =============");
            System.out.println(jsonString);
            
            jsonString = URLEncoder.encode(jsonString);
            System.out.println("============== 변환하고 후 =============");
            System.out.println(jsonString);
            
            BufferedReader in = null;
            
            try {
                URL obj = new URL(jsonString);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                con.setRequestMethod("GET");
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                while((line = in.readLine()) != null) { // response를 차례대로 출력
                    System.out.println(line);
                }
            } catch(Exception e) {
                // e.printStackTrace();
            } finally {
                if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
            }
    	}

    	
    	

        
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}