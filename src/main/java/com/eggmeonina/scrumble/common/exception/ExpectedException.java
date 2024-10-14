package com.eggmeonina.scrumble.common.exception;

import lombok.Getter;

@Getter
public class ExpectedException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int statusCodeValue;
	private final String message;

	public ExpectedException(ErrorCode errorCode){
		this.errorCode = errorCode;
		this.statusCodeValue = errorCode.getStatus().value();
		this.message = errorCode.getMessage();
	}
}
