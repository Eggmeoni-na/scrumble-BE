package com.eggmeonina.scrumble.domain.auth.facade;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.AuthException;
import com.eggmeonina.scrumble.domain.auth.client.AuthClient;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.auth.dto.OauthRequest;
import com.eggmeonina.scrumble.domain.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceTest {

	private AuthFacadeService authFacadeService;

	@Mock
	private AuthClient authClient;

	@Mock
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		authFacadeService = new AuthFacadeService(Map.of(OauthType.GOOGLE.getAuthClientName(), authClient), memberService);
	}

	@Test
	@DisplayName("토큰값을 요청한다_성공")
	void getToken_success() {
		// given
		OauthRequest request = new OauthRequest(OauthType.GOOGLE, "code", "scope");
		GoogleAuthClientResponse token
			= new GoogleAuthClientResponse("accessToken", 1000, "tokenType", "scope", "token");
		MemberInformation information = new MemberInformation("1233245", "test@test.com", "testA", "");
		LoginMember mockloginMember = new LoginMember(1L, information.getEmail(), information.getName());

		given(authClient.getAccessToken(any(), any())).willReturn(token);
		given(authClient.findUserProfile(any())).willReturn(information);
		given(memberService.login(any(), any())).willReturn(mockloginMember);

		// when
		LoginMember actualLoginMember = authFacadeService.getToken(request);

		// then
		assertSoftly(softly -> {
			softly.assertThat(actualLoginMember.getMemberId()).isEqualTo(mockloginMember.getMemberId());
			softly.assertThat(actualLoginMember.getName()).isEqualTo(mockloginMember.getName());
			softly.assertThat(actualLoginMember.getEmail()).isEqualTo(mockloginMember.getEmail());
		});
	}

	@Test
	@DisplayName("지원되지 않는 OauthType으로 토큰 값을 요청한다_실패")
	void getTokenWhenNotExistsOAuthType_fail() {
		// given
		OauthRequest request = new OauthRequest(OauthType.GOOGLE, "code", "scope");

		// authFacadeService에 주입된 authClient를 초기화하여 테스트
		Map<String, AuthClient> authClientMap = new HashMap<>();
		authFacadeService = new AuthFacadeService(authClientMap, memberService);

		// when, then
		assertThatThrownBy(() -> authFacadeService.getToken(request))
			.isInstanceOf(AuthException.class)
			.hasMessageContaining(TYPE_NOT_SUPPORTED.getMessage());
	}
}
