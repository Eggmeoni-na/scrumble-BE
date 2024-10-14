package com.eggmeonina.scrumble.common.exception;

public class MemberException extends ExpectedException{
	public MemberException(ErrorCode errorCode) {
		super(errorCode);
	}
}
