package com.eggmeonina.scrumble.domain.auth.client;

import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientResponse;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;

public interface AuthClient {

	// TODO : oauth 로그인 수단 추가 시, 추상화된 응답 객체로 수정한다.
	GoogleAuthClientResponse getAccessToken(String code, String state);

	MemberInformation findUserProfile(String header);
}
