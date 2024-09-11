package com.eggmeonina.scrumble.domain.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.controller.generator.OauthGenerator;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.OauthRequest;
import com.eggmeonina.scrumble.domain.auth.dto.SessionMember;
import com.eggmeonina.scrumble.domain.auth.facade.AuthFacadeService;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Tag(name = "Auth 테스트", description = "oauth, 회원가입, 로그인 테스트용 API Controller")
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final Map<String, OauthGenerator> oauthGenerators;
	private final AuthFacadeService authFacadeService;

	@Autowired
	public AuthController(Map<String, OauthGenerator> oauthGenerators, AuthFacadeService authFacadeService) {
		this.oauthGenerators = oauthGenerators;
		this.authFacadeService = authFacadeService;
	}

	@GetMapping("/oauth-url")
	@Operation(summary = "oauth 서버 요청용 request url 조회", description = "oauth 서버에 요청할 request url을 조합하여 반환해주는 메서드입니다.",
		parameters = @Parameter(name = "oauthType", description = "oauth type || GOOGLE : 구글"))
	public ApiResponse<Map<String, String>> getOauthUrl(@RequestParam OauthType oauthType) {
		Map<String, String> response = new HashMap<>();

		OauthGenerator generator = oauthGenerators.get(oauthType.getGeneratorName());
		if (generator == null) {
			throw new RuntimeException("지원하지 않는 type입니다.");
		}
		response.put("request-url", generator.getUrl());
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), response);
	}

	@PostMapping("/login")
	@Operation(summary = "OAuth2 authorization_code를 받아 로그인/회원가입을 한다", description = "OAuth2 Authorization Code Grant 로그인",
		parameters = {@Parameter(name = "oauthType", description = "oauth type || GOOGLE : 구글로그인"),
		@Parameter(name = "code", description = "oauth 서버에서 응답된 code"),
		@Parameter(name = "scope", description = "oauth 서버에서 응답된 scope")})
	public ApiResponse<Void> login(
		HttpServletRequest servletRequest,
		@RequestBody OauthRequest request
	) {
		SessionMember sessionMember = authFacadeService.getToken(request);
		HttpSession session = servletRequest.getSession(true);
		session.setAttribute(SessionKey.LOGIN_USER.name(), sessionMember);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PostMapping("/logout")
	@Operation(summary = "회원의 로그아웃을 진행한다.", description = "로그아웃을 위해 세션을 만료한다.")
	public ApiResponse<Void> logout(HttpServletRequest servletRequest){
		HttpSession session = servletRequest.getSession(false);
		if (session == null) {
			throw new IllegalStateException("유효하지 않은 요청입니다.");
		}
		session.invalidate();
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}
}
