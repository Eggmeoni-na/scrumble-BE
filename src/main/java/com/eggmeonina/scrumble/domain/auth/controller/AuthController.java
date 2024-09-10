package com.eggmeonina.scrumble.domain.auth.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.domain.auth.controller.generator.OauthGenerator;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth 테스트", description = "oauth, 회원가입, 로그인 테스트용 API Controller")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final Map<String, OauthGenerator> oauthGenerator;

	@Autowired
	public AuthController(Map<String, OauthGenerator> oauthGenerator) {
		this.oauthGenerator = Collections.unmodifiableMap(oauthGenerator);
	}

	@GetMapping("/oauth-url")
	@Operation(summary = "oauth 서버 요청용 request url 조회", description = "oauth 서버에 요청할 request url을 조합하여 반환해주는 메서드입니다.",
		parameters = @Parameter(name = "oauthType", description = "oauth type || GOOGLE : 구글"))
	public ResponseEntity<Map<String, String>> getOauthUrl(@RequestParam OauthType oauthType){
		Map<String, String> response = new HashMap<>();

		OauthGenerator generator = oauthGenerator.get(oauthType.getGeneratorName());
		if (generator == null) {
			response.put("error", "지원하지 않는 oauth 타입입니다.");
			return ResponseEntity.badRequest().body(response);
		}
		response.put("request-url", generator.getUrl());
		return ResponseEntity.ok().body(response);
	}
}
