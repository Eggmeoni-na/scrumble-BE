package com.eggmeonina.scrumble.common.exception;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eggmeonina.scrumble.common.domain.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// @Value("${exception.ignored-broken-pipe-uris}")
	private static final List<String> IGNORED_BROKEN_PIPE_URIS = List.of("/api/notifications/subscribe");

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
		String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
		log.debug("req.requestURI = {}, req.getMethod() = {}", req.getRequestURI(), req.getMethod());
		// 특정 uri에서 발생하는 broken pipe 에러는 warning 레벨로 관리
		if (rootCauseMessage.contains("Broken pipe") && IGNORED_BROKEN_PIPE_URIS.contains(req.getRequestURI())) {
			log.warn("broken pipe error!! client close connect = {}", rootCauseMessage);
			return;
		}
		log.error("e = {}", e.toString(), e);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		this.log(e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.createErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}

	private void log(Exception e){
		log.warn("{}", e.toString(), e);
	}
}
