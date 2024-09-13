package com.eggmeonina.scrumble.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eggmeonina.scrumble.common.domain.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExpectedException.class)
	public ResponseEntity<ApiResponse<Void>> customHandlerException(ExpectedException e){
		this.log(e);
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(ApiResponse.createErrorResponse(e.getStatusCodeValue(), e.getMessage()));

	}

	private void log(ExpectedException e) {
		log.error("{}", e.toString());
	}
}
