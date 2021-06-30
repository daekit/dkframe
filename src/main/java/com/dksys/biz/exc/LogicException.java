package com.dksys.biz.exc;

@SuppressWarnings("serial")
public class LogicException extends Exception{
	
	String message = null;
	
	public LogicException(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
