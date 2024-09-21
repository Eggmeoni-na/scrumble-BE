package com.eggmeonina.scrumble.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * 공통
	 */
	ERROR(HttpStatus.BAD_REQUEST, "오류가 발생했습니다."),
	FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "오류가 발생했습니다."),

	/**
	 * Auth
	 */
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "허용할 수 없는 접근입니다."),
	TYPE_NOT_SUPPORTED(HttpStatus.NOT_FOUND, "지원하지 않는 로그인 수단입니다."),

	/**
	 * 회원
	 */
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	MEMBER_ALREADY_WITHDRAW(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),

	/**
	 * 스쿼드
	 */
	MEMBER_OR_SQUAD_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원 또는 스쿼드가 누락되었습니다."),
	SQUAD_NOT_FOUND(HttpStatus.BAD_REQUEST, "스쿼드가 누락되었습니다.");

	private final HttpStatus status;
	private final String message;
}
