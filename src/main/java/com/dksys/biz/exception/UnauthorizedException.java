package com.dksys.biz.exception;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = -2238030302650813813L;
	
	public UnauthorizedException(String msg) {
		super(msg);
	}
	
}