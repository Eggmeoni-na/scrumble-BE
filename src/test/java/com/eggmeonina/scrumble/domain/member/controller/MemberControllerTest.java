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

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MemberException;
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

	@Test
	@DisplayName("회원 조회 실패 시, not found 오류가 발생한다.")
	void findMember_fail_throwsNotFoundException() throws Exception {
		// given
		MemberResponse response = new MemberResponse(OauthType.GOOGLE, "test@test.com");
		given(memberService.findMember(1L)).willThrow(new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
			SessionKey.LOGIN_USER.name(), new LoginMember(1L, response.getEmail(), "test")
		);

		// when, then
		mockMvc.perform(get("/members/me").session(session))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("회원 탈퇴 요청 시, 세션이 만료된다")
	void withdrawMember_success_InvalidSession() throws Exception {
		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
			SessionKey.LOGIN_USER.name(), new LoginMember(1L, "test@test.com", "test")
		);
		
		// when, then
		mockMvc.perform(delete("/members").session(session))
			.andExpect(request().sessionAttributeDoesNotExist(SessionKey.LOGIN_USER.name())) // 세션 만료 확인
			.andDo(print())
			.andExpect(status().isOk());
	}

}
