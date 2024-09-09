package com.eggmeonina.scrumble.domain.auth.controller.generator;

import org.springframework.web.util.UriComponentsBuilder;

import com.eggmeonina.scrumble.domain.auth.properties.GoogleProperties;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class GoogleGenerator implements OauthGenerator {

	private final GoogleProperties googleProperties;
	@Override
	public String getUrl() {
		return UriComponentsBuilder
			.fromUriString(googleProperties.getRequestUri())
			.queryParam("client_id", googleProperties.getClientId())
			.queryParam("redirect_uri", googleProperties.getRedirectUri())
			.queryParam("response_type", googleProperties.getResponseType())
			.queryParam("scope", googleProperties.getScope())
			.toUriString();
	}
}
