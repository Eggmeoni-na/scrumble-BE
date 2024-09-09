package com.eggmeonina.scrumble.domain.auth.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.domain.auth.controller.generator.OauthGenerator;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

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
	public ResponseEntity<String> getOauthUrl(@RequestParam OauthType type){
		OauthGenerator generator = oauthGenerator.get(type.getGeneratorName());
		if (generator == null) {
			return ResponseEntity.badRequest().body("지원하지 않는 로그인 타입입니다.");
		}
		return ResponseEntity.ok().body(generator.getUrl());
	}
}
