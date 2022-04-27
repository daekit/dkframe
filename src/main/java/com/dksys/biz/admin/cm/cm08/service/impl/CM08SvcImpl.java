package com.dksys.biz.admin.cm.cm08.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.mapper.CM08Mapper;
import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.util.DateUtil;

@Service
public class CM08SvcImpl implements CM08Svc {
	
    @Autowired
    CM08Mapper cm08Mapper;

	@Override
	public int uploadFile(String fileTrgtTyp, String fileTrgtKey, MultipartHttpServletRequest mRequest) {
		List<MultipartFile> fileList = mRequest.getFiles("files");
		String year = DateUtil.getCurrentYyyy();
		String month = DateUtil.getCurrentMm();
        String path = "D:\\goldmoon\\upload" + File.separator + fileTrgtTyp + File.separator + year + File.separator + month + File.separator;
        for (MultipartFile mf : fileList) {
            String originFileName = mf.getOriginalFilename(); // 원본 파일 명
            // long fileSize = mf.getSize(); // 파일 사이즈

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("fileSize", String.valueOf(mf.getSize()));
            param.put("fileType", originFileName.split("\\.")[originFileName.split("\\.").length-1]);
            param.put("fileName", originFileName);
            param.put("filePath", path);
            param.put("fileTrgtTyp", fileTrgtTyp);
            param.put("fileTrgtKey", fileTrgtKey);
            param.put("userId", mRequest.getParameter("userId"));
            param.put("pgmId", fileTrgtTyp);
            try {
            	cm08Mapper.insertFile(param);
            	String saveFile = param.get("fileKey") + "_" + originFileName;
            	File f = new File(path);
            	if(!f.isDirectory()) f.mkdirs();
            	
            	if(fileTrgtTyp.equals("TB_OD01M01")) {
            		mf.transferTo(new File(path+saveFile));
            	}
            	
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return 0;
	}

	@Override
	public List<Map<String, String>> selectFileList(Map<String, String> paramMap) {
		return cm08Mapper.selectFileList(paramMap);
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

	@Override
	public int deleteFile(String fileKey) {
		Map<String, String> fileInfo = selectFileInfo(fileKey);
		String filePath = fileInfo.get("filePath") + fileKey + "_" + fileInfo.get("fileName");
		int result = cm08Mapper.deleteFileInfo(fileKey);
		try {
			File f = new File(filePath);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String excelDownload(List<Map<String, String>> list, String fileName) {
		if(list.size() == 0) return "noData";
		try (XSSFWorkbook xWorkbook = new XSSFWorkbook()) {
			XSSFCellStyle numberCellStyle = xWorkbook.createCellStyle();
			XSSFDataFormat numberDataFormat = xWorkbook.createDataFormat();
			numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));
			XSSFSheet sheet = xWorkbook.createSheet("sheet1");
			Row row = null;
			int j = 0;
			//컬럼 생성
			row = sheet.createRow(0);
			Map<String, String> map = list.get(0);
			//컬럼 생성
			for(String key : map.keySet()) {
				Cell cell = row.createCell(j);
				cell.setCellValue(key);
				j++;
			}
			//행 데이터 생성
			for (int i = 0; i < list.size(); i++) {
				j = 0;
				map = list.get(i);
				row = sheet.createRow(i+1);
				for(String key : map.keySet()) {
					Cell cell = row.createCell(j);
					cell.setCellValue(map.get(key));
					j++;
				}
			}
			FileOutputStream fileOut = null;
	        String path = "C:\\upload\\" + fileName;
			File f = new File(path);
//        	if(!f.isDirectory()) f.mkdirs();
        	fileOut = new FileOutputStream(f);
        	xWorkbook.write(fileOut);
      		f.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
}