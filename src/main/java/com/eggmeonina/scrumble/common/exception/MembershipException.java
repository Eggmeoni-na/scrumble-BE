package com.eggmeonina.scrumble.common.exception;

public class MembershipException extends ExpectedException{
	public MembershipException(ErrorCode errorCode) {
		super(errorCode);
	}
}
