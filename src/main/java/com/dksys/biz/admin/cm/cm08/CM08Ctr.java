package com.dksys.biz.admin.cm.cm08;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/cm/cm08")
public class CM08Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	CM08Svc cm08Svc;
	
	@PostMapping(value="/fileDownInfo")
	public String fileDownInfo(@RequestBody Map<String, String> param, ModelMap model) throws Exception {
		Map<String, String> fileInfo = cm08Svc.selectFileInfo(param.get("fileKey"));
		model.addAttribute("fileInfo", fileInfo);
		return "jsonView";
	}
	
	@GetMapping(value="/fileDownload")
	public void fileDownload(@RequestParam String filePath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BufferedInputStream bis = null;
		ServletOutputStream sos = null;

		try{
			File file = new File(filePath);
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.setContentLength((int)file.length());
			int idx = filePath.split("\\\\").length-1;
			String fileName = filePath.split("\\\\")[idx];
			fileName = fileName.substring(fileName.indexOf("_")+1);
			cm08Svc.setDisposition(request, response, fileName);

			OutputStream out = response.getOutputStream();
	        FileInputStream fis = null;
	         
	        try {
	            fis = new FileInputStream(file);
	            FileCopyUtils.copy(fis, out);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (fis != null) { try { fis.close(); } catch (Exception e2) {}}
	            if (out != null) { try { out.close(); } catch (Exception e2) {}}
	        }
	        out.flush();
	        
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			if(bis != null)
				bis.close();
			if(sos != null)
				sos.close();
		}
	}
    
	@GetMapping(value="/excelDownload")
	public void excelDownload(@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BufferedInputStream bis = null;
		ServletOutputStream sos = null;
		String filePath = "C:\\upload\\";
		try{
			File file = new File(filePath+fileName);
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.setContentLength((int)file.length());
			cm08Svc.setDisposition(request, response, fileName);

			OutputStream out = response.getOutputStream();
	        FileInputStream fis = null;
	         
	        try {
	            fis = new FileInputStream(file);
	            FileCopyUtils.copy(fis, out);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (fis != null) { try { fis.close(); } catch (Exception e2) {}}
	            if (out != null) { try { out.close(); } catch (Exception e2) {}}
	        }
	        out.flush();
	        
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			if(bis != null)
				bis.close();
			if(sos != null)
				sos.close();
		}
	}
}