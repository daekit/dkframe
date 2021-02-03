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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping(value="/fileDownload")
	public void fileDownload(@RequestParam("fileKey") String fileKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, String> fileInfo = cm08Svc.selectFileInfo(fileKey);
		
		if (fileInfo != null) {

			BufferedInputStream bis = null;
			ServletOutputStream sos = null;

			try{
				File file = new File(fileInfo.get("filePath"));
				String fileName = fileInfo.get("fileName");
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
    
}