//package com.dksys.biz.sap;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dksys.biz.sap.service.SapService;
//import com.sap.conn.jco.JCoException;
//
//@RestController
//public class SapTestController {
//	
//	@Autowired
//    private SapService sapService;
//	
//	// SAP테스트
//    @PostMapping("/sapTest")
//    public String sapTest() {
//    	try {
//			sapService.getSapManager("dks");
//		} catch (JCoException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	return "SUCCESS";
//    }	
//	
//}