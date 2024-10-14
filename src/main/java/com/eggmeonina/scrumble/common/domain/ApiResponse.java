package com.eggmeonina.scrumble.common.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
	private int statusCodeValue;
	private String message;
	private T data;

	public static <T> ApiResponse<T> createSuccessResponse(int statusCodeValue, T data){
		return new ApiResponse<>(statusCodeValue, null, data);
	}

	public static <T> ApiResponse<T> createSuccessWithNoContentResponse(int statusCodeValue){
		return new ApiResponse<>(statusCodeValue, null, null);
	}

	public static <T> ApiResponse<T> createErrorResponse(int statusCodeValue, String message){
		return new ApiResponse<>(statusCodeValue, message, null);
	}

	private ApiResponse(int statusCodeValue, String message, T data) {
		this.statusCodeValue = statusCodeValue;
		this.message = message;
		this.data = data;
	}
}
