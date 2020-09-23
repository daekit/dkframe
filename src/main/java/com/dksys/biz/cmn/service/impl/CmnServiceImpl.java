package com.dksys.biz.cmn.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dksys.biz.cmn.service.CmnService;

@Service
@SuppressWarnings("all")
public class CmnServiceImpl implements CmnService {

	@Override
	public List<Map<String, String>> uploadExcelFile(MultipartFile excelFile) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			OPCPackage opcPackage = OPCPackage.open(excelFile.getInputStream());
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			for (int i = 0; i < sheet.getLastRowNum()+1; i++) {
				Map<String, String> map = new HashMap<String, String>();
				Row row = sheet.getRow(i);
				if(null == row) {		
					continue;
				}
				for (int j = 0; j < row.getLastCellNum(); j++) {
					map.put("key"+(j+1), row.getCell(j).toString());
				}
				list.add(map);
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	

}