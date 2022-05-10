package com.dksys.biz.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.exc.LogicException;

@Component
public class ExceptionThrower{
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	BM01Mapper bm01Mapper;
	
	public void throwCommonException(String msgKey) throws LogicException {
		throw new LogicException(messageUtils.getMessage(msgKey));
	}
	
	public void throwCreditLoanException(String prdtGrp, Long diffLoan) throws LogicException{
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		diffLoan *= -1;
		String diffLoanStr = numberFormat.format(diffLoan);
		String prdtGrpNm = bm01Mapper.selectProductGroupNm(prdtGrp);
		
		String message = "";
		if(prdtGrpNm != null) {
			message += prdtGrpNm + "그룹의 ";
		}
		message += "여신이" + diffLoanStr + "원 만큼 부족합니다."; 
		throw new LogicException(message);
	}
	
	public void throwCreditLoanExceptionBig(String prdtGrp, BigDecimal diffLoan) throws LogicException{
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		diffLoan = diffLoan.multiply(new BigDecimal("-1"));
		
		String diffLoanStr = numberFormat.format(diffLoan);
		String prdtGrpNm = bm01Mapper.selectProductGroupNm(prdtGrp);
		
		String message = "";
		if(prdtGrpNm != null) {
			message += prdtGrpNm + "그룹의 ";
		}
		message += "여신이" + diffLoanStr + "원 만큼 부족합니다."; 
		throw new LogicException(message);
	}
	
}
