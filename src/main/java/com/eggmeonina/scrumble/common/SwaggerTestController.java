package com.eggmeonina.scrumble.common;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.domain.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile("!prod")
@Tag(name = "스웨거 테스트", description = "swagger 테스트용 API Controller")
@RestController
@RequestMapping("/api/test")
public class SwaggerTestController {

	@GetMapping("response-string")
	@Operation(summary = "String 응답", description = "테스트용 파라미터(code)를 요청하면 String으로 응답하는 테스트용 메서드입니다.")
	public ApiResponse<String> responseString(@Parameter(description = "테스트용 파라미터") String code){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), code);
	}

	@GetMapping("response-obj")
	@Operation(summary = "객체 응답", description = "테스트용 파라미터(code)를 요청하면 객체로 응답하는 테스트용 메서드입니다. 에러 코드 스키마는 예시입니다.(미정)")
	public ApiResponse<SwaggerTestObject> responseObject(@Parameter(description = "테스트용 파라미터") String code){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), new SwaggerTestObject("scrumble", 0));
	}

	@GetMapping("response-no-data")
	@Operation(summary = "응답 객체 없음", description = "테스트용 파라미터(code)를 요청하면 응답은 하나 data가 없는 테스트용 메서드입니다.")
	public ApiResponse<Void> responseNoData(@Parameter(description = "테스트용 파라미터")String code){
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}
}
