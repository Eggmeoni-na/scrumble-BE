package com.eggmeonina.scrumble.domain.auth.client;

import com.eggmeonina.scrumble.domain.auth.client.strategy.GoogleRequestResourceStrategy;
import com.eggmeonina.scrumble.domain.auth.client.strategy.GoogleRequestTokenStrategy;
import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientRequest;
import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientResponse;
import com.eggmeonina.scrumble.domain.auth.dto.GrantType;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.auth.properties.GoogleProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GoogleAuthClient implements AuthClient {

	private final GoogleProperties googleProperties;
	private final GoogleRequestTokenStrategy requestTokenStrategy;
	private final GoogleRequestResourceStrategy resourceStrategy;

	@Override
	public GoogleAuthClientResponse getAccessToken(String code, String state) {
		return requestTokenStrategy.getAccessToken(
			new GoogleAuthClientRequest
				(
					googleProperties.getClientId(),
					googleProperties.getClientKey(),
					code,
					GrantType.AUTHORIZATION_CODE.name().toLowerCase(),
					googleProperties.getRedirectUri()
				));
	}

	@Override
	public MemberInformation findUserProfile(String header) {
		return resourceStrategy.findUserInfo(header);
	}
}
