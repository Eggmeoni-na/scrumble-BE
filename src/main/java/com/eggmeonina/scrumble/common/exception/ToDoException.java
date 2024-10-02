package com.eggmeonina.scrumble.common.exception;

public class ToDoException extends ExpectedException{

	public ToDoException(ErrorCode errorCode) {
		super(errorCode);
	}
}
