package com.eggmeonina.scrumble.domain.auth.controller.generator;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.domain.auth.properties.GoogleProperties;

@ExtendWith(MockitoExtension.class)
class GoogleGeneratorTest {

	@InjectMocks
	private GoogleGenerator generator;
	@Mock
	private GoogleProperties properties;

	@Test
	@DisplayName("url을 요청하면 url을 String 타입으로 반환한다")
	void getUrl_success_returnsUrlString() {
		// given
		String requestUri = "https://accounts.google.com/o/oauth2/v2/auth";
		String clientUrl = "test_client_id";
		String redirectUri = "test_redirect_uri";
		String responseType = "code";
		String scope = "profile";

		given(properties.getRequestUri()).willReturn(requestUri);
		given(properties.getClientId()).willReturn(clientUrl);
		given(properties.getResponseType()).willReturn(responseType);
		given(properties.getRedirectUri()).willReturn(redirectUri);
		given(properties.getScope()).willReturn(scope);

		// when
		String generatorUrl = generator.getUrl();

		// then
		assertThat(generatorUrl)
			.isEqualTo("https://accounts.google.com/o/oauth2/v2/auth?client_id=test_client_id&redirect_uri=test_redirect_uri&response_type=code&scope=profile");
	}
}
