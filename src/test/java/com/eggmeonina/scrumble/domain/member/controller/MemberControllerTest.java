package com.eggmeonina.scrumble.domain.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.service.MemberService;
import com.eggmeonina.scrumble.helper.WebMvcTestHelper;

class MemberControllerTest extends WebMvcTestHelper {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName("회원 조회 시, 회원을 응답한다.")
	void findMember_success_returnsMember() throws Exception {
		// given
		MemberResponse response = new MemberResponse(OauthType.GOOGLE, "test@test.com");
		given(memberService.findMember(1L)).willReturn(response);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
			SessionKey.LOGIN_USER.name(), new LoginMember(1L, response.getEmail(), "test")
		);

		// when, then
		mockMvc.perform(get("/members/me").session(session))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.oauthType").value(response.getOauthType().name()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(response.getEmail()))
			.andDo(print())
			.andExpect(status().isOk());
	}

	//TODO : 에러 핸들러가 추가되면 실패한 경우도 구현


}
