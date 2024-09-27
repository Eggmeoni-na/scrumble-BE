package com.eggmeonina.scrumble.common.exception;

public class SquadMemberException extends ExpectedException{
	public SquadMemberException(ErrorCode errorCode) {
		super(errorCode);
	}
}
