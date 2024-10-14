package com.eggmeonina.scrumble.domain.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.google")
public class GoogleProperties {
	private final String clientId;
	private final String clientKey;
	private final String redirectUri;
	private final String responseType;
	private final String scope;

	// oauth에 요청 시 보내는 uri 정의
	private final String requestUri;
	private final String tokenUri;
	private final String resourceUri;
}
