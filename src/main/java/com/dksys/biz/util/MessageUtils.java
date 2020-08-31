package com.dksys.biz.util;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
 
public class MessageUtils {

	@Autowired
	MessageSource messageSource;
	
	public String getMessage(String code) {
		return messageSource.getMessage(code, new String[] {}, Locale.getDefault());
	}
	
	
}