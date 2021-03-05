//package com.dksys.biz.sap.service.impl;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.dksys.biz.property.SapProperty;
//import com.dksys.biz.sap.service.SapService;
//import com.sap.conn.jco.JCoException;
//
//import me.saro.sap.jco.SapManager;
//import me.saro.sap.jco.SapManagerBuilderOption;
//
//
//@Service
//public class SapServiceImpl implements SapService {
//
//	@Autowired
//    private SapProperty sapDksProperty;
//	
//	public SapManager getSapManager(String moduleName) throws JCoException, IOException {
//        return SapManager
//                .builder()
//                .set(SapManagerBuilderOption.ASHOST, sapDksProperty.getDksHost()) 	// AS host
//                .set(SapManagerBuilderOption.SYSNR, sapDksProperty.getDksSysnr()) 	// system number
//                .set(SapManagerBuilderOption.LANG, sapDksProperty.getDksLang()) 	// language code
//                .set(SapManagerBuilderOption.CLIENT, sapDksProperty.getDksClient()) // client number
//                .set(SapManagerBuilderOption.USER, sapDksProperty.getDksUser()) 	// user
//                .set(SapManagerBuilderOption.PASSWD, sapDksProperty.getDksPasswd()) // password
//                .set(SapManagerBuilderOption.POOL_CAPACITY, "200") 					// Maximum number of idle connections kept open by the destination. A value of 0 has the effect that there is no connection pooling
//                .set(SapManagerBuilderOption.PEAK_LIMIT, "1000") 					// Maximum number of active connections that can be created for a destination simultaneously
//                .set(SapManagerBuilderOption.EXPIRATION_TIME, "10000") // 
////                .set(SapManagerBuilderOption.EXPIRATION_PERIOD, "1000") // 
////                .set(SapManagerBuilderOption.MSSERV, "9999") // MS port [AS, MS is MSSERV, GW is JCO_GWSERV]
////                .set(SapManagerBuilderOption.GROUP, "DKS_EMPLOYEE_INFO") // group
//                .build();
//    }
//	
//}
