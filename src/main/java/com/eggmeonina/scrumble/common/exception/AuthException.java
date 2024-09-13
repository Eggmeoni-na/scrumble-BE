package com.eggmeonina.scrumble.common.exception;

public class AuthException extends ExpectedException {
	public AuthException(ErrorCode errorCode) {
		super(errorCode);
	}
}
