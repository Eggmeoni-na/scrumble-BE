package com.eggmeonina.scrumble.common.exception;

public class SquadException extends ExpectedException{
	public SquadException(ErrorCode errorCode) {
		super(errorCode);
	}
}
