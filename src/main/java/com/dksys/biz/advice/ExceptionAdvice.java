package com.dksys.biz.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
	public ModelAndView illegalArgumentException(HttpServletRequest req, Exception e) {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("url", req.getRequestURL());
		mav.addObject("msg", e.getMessage());
		return mav;
	}

}