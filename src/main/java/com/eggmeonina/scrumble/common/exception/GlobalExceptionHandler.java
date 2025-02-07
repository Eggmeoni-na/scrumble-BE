package com.eggmeonina.scrumble.common.exception;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eggmeonina.scrumble.common.domain.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExpectedException.class)
	public ResponseEntity<ApiResponse<Void>> customHandlerException(ExpectedException e) {
		this.log(e);
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(ApiResponse.createErrorResponse(e.getStatusCodeValue(), e.getMessage()));

	}

	/**
	 * SSEEmitter에서 클라이언트의 연결이 종료되면 Broken pipe 오류가 발생한다.
	 * Broken pipe 예외를 핸들링하고 이외의 IOException은 에러 로그를 출력한다.
	 * @param e
	 * @param req
	 */
	@ExceptionHandler(IOException.class)
	public void handlerIOException(IOException e, HttpServletRequest req) {
		log.debug("req.requestURI = {}, req.getMethod() = {}", req.getRequestURI(), req.getMethod());
		if (ExceptionUtils.getRootCauseMessage(e).contains("Broken pipe") &&
			req.getRequestURI().equals("/api/notifications/subscribe")) {
			log.warn("broken pipe error!! client close connect = {}", ExceptionUtils.getRootCauseMessage(e));
			return;
		}
		log.error("IOException rootCauseMessage = {}", ExceptionUtils.getRootCauseMessage(e));
		e.printStackTrace();
	}

	private void log(ExpectedException e) {
		log.error("{}", e.toString());
	}
}
