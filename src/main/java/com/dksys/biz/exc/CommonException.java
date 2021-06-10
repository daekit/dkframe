package com.dksys.biz.exc;

@SuppressWarnings("serial")
public class CommonException extends Exception{
	
	String message = null;
	
	public CommonException(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
