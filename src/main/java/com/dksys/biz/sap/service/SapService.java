package com.dksys.biz.sap.service;

import java.io.IOException;

import com.sap.conn.jco.JCoException;

import me.saro.sap.jco.SapManager;

public interface SapService {

	public SapManager getSapManager(String moduleName) throws JCoException, IOException;

	
}