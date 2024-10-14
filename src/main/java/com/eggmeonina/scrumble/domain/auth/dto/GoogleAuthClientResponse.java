package com.eggmeonina.scrumble.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class GoogleAuthClientResponse {

	private final String accessToken;
	private final int expiresIn;
	private final String tokenType;
	private final String scope;
	private final String refreshToken;

	@JsonCreator
	public GoogleAuthClientResponse(
		@JsonProperty("access_token") String accessToken,
		@JsonProperty("expires_in") int expiresIn,
		@JsonProperty("token_type") String tokenType,
		@JsonProperty("scope") String scope,
		@JsonProperty("refresh_token") String refreshToken
	) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
		this.scope = scope;
		this.refreshToken = refreshToken;
	}
}
