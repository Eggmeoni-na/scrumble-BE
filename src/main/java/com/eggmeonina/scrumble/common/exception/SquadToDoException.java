package com.eggmeonina.scrumble.common.exception;

public class SquadToDoException extends ExpectedException{
	public SquadToDoException(ErrorCode errorCode) {
		super(errorCode);
	}
}
