package com.dksys.biz.admin.cm.cm08.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.mapper.CM08Mapper;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;

@Service
public class CM08SvcImpl implements CM08Svc {
	
    @Autowired
    CM08Mapper cm08Mapper;

	@Override
	public int uploadFile(String fileTrgtTyp, String fileTrgtKey, MultipartHttpServletRequest mRequest) {
		List<MultipartFile> fileList = mRequest.getFiles("files");
		String src = mRequest.getParameter("src");
        System.out.println("src value : " + src);

        String path = "C:/upload/";

        for (MultipartFile mf : fileList) {
            String originFileName = mf.getOriginalFilename(); // 원본 파일 명
            long fileSize = mf.getSize(); // 파일 사이즈

            System.out.println("originFileName : " + originFileName);
            System.out.println("fileSize : " + fileSize);

            String safeFile = path + System.currentTimeMillis() + originFileName;
            
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("fileSize", String.valueOf(mf.getSize()));
            param.put("fileType", originFileName.split("\\.")[originFileName.split("\\.").length-1]);
            param.put("fileName", originFileName);
            param.put("filePath", path+originFileName);
            param.put("fileTrgtTyp", fileTrgtTyp);
            param.put("fileTrgtKey", fileTrgtKey);
            param.put("userId", mRequest.getParameter("userId"));
            param.put("pgmId", mRequest.getRequestURI());
            try {
            	cm08Mapper.insertFile(param);
            	File dir = new File(path);
            	if(!dir.isDirectory()) dir.mkdirs();
            	mf.transferTo(new File(safeFile));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return 0;
	}

	@Override
	public List<Map<String, String>> selectFileList(String fileTrgtKey) {
		return cm08Mapper.selectFileList(fileTrgtKey);
	}

	@Override
	public void setDisposition(HttpServletRequest request, HttpServletResponse response, String fileName) {
		try {
			String browser = getBrowser(request);
			String dispositionPrefix = "attachment; filename=";
			String encodedFilename = null;
			if (browser.equals("MSIE")) {
			    encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
			    encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (browser.equals("Firefox")) {
			    encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (browser.equals("Opera")) {
			    encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (browser.equals("Chrome")) {
			    StringBuffer sb = new StringBuffer();
			    for (int i = 0; i < fileName.length(); i++) {
			        char c = fileName.charAt(i);
			        if (c > '~') {
			            sb.append(URLEncoder.encode("" + c, "UTF-8"));
			        } else {
			            sb.append(c);
			        }
			    }
			    encodedFilename = "\"" + sb.toString() + "\"";
			} else {
			    throw new IOException("Not supported browser");
			}
			response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
			if ("Opera".equals(browser)) {
			    response.setContentType("application/octet-stream;charset=UTF-8");
			}
			response.setHeader("Content-Transfer-Encoding", "binary");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

	@Override
	public Map<String, String> selectFileInfo(String fileKey) {
		return cm08Mapper.selectFileInfo(fileKey);
	}
}