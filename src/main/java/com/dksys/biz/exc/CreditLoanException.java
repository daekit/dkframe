package com.dksys.biz.exc;

@SuppressWarnings("serial")
public class CreditLoanException extends Exception{
	
	String message = null;
	
	public CreditLoanException(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
