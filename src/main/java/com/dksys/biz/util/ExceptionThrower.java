package com.dksys.biz.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dksys.biz.exc.LogicException;

@Component
public class ExceptionThrower{
	@Autowired
	MessageUtils messageUtils;
	
	public void throwCommonException(String msgKey) throws LogicException {
		throw new LogicException(messageUtils.getMessage(msgKey));
	}
	
	public void throwCreditLoanException(String prdtGrpNm, Long diffLoan) throws LogicException{
		diffLoan *= -1;
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		String message = "";
		if(!"".equals(prdtGrpNm)) {
			message += prdtGrpNm + " 그룹의 ";
		}
		message += "여신이" + numberFormat.format(diffLoan) + "원 만큼 부족합니다."; 
		throw new LogicException(message);
	}
	
}
