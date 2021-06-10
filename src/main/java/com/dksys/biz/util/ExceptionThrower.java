package com.dksys.biz.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dksys.biz.exc.CommonException;
import com.dksys.biz.exc.CreditLoanException;

@Component
public class ExceptionThrower{
	@Autowired
	MessageUtils messageUtils;
	
	public void throwCommonException(String msgKey) throws CommonException {
		throw new CommonException(messageUtils.getMessage(msgKey));
	}
	
	public void throwCreditLoanException(Long diffLoan) throws CreditLoanException{
		diffLoan *= -1;
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		String message = numberFormat.format(diffLoan) + "원 만큼 여신이 부족합니다.";
		throw new CreditLoanException(message);
	}
}
