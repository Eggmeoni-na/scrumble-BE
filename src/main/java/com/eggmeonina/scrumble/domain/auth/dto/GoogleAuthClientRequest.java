package com.eggmeonina.scrumble.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthClientRequest {

	private String clientId;
	private String clientSecret;
	private String code;
	private String grantType;
	private String redirectUri;
}
