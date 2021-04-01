package com.dksys.biz.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

@Component("excelDown")
public class ExcelDown extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try (XSSFWorkbook xWorkbook = new XSSFWorkbook()) {
			response.setHeader("Content-Disposition", "attachment; filename=\"sample.xlsx\"");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			XSSFCellStyle numberCellStyle = xWorkbook.createCellStyle();
			XSSFDataFormat numberDataFormat = xWorkbook.createDataFormat();
			numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));
			XSSFSheet sheet = xWorkbook.createSheet("sheet1");
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) model.get("excelData");
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
			xWorkbook.write(response.getOutputStream());
			response.getOutputStream().close();
		}
	}

}

