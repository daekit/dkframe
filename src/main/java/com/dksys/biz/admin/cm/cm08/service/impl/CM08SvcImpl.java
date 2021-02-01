package com.dksys.biz.admin.cm.cm08.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
	public int uploadFile(String fileTrgtKey, MultipartHttpServletRequest mRequest) {
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
            param.put("fileType", originFileName.substring(originFileName.indexOf(".")+1));
            param.put("fileName", originFileName);
            param.put("filePath", path);
            param.put("fileTrgtTyp", mRequest.getRequestURI().split("/")[mRequest.getRequestURI().split("/").length-1]);
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

}