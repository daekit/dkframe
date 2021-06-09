package com.dksys.biz.exc;

import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("serial")
public class CreditLoanException extends Exception{
	
	String message = null;
	
	public CreditLoanException(Long diffLoan) {
		super();
		diffLoan *= -1;
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		message = numberFormat.format(diffLoan) + "원 만큼 여신이 부족합니다.";
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
