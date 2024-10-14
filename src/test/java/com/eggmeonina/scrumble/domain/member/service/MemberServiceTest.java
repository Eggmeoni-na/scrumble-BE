package com.eggmeonina.scrumble.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.dto.MemberInvitationResponse;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이메일로 회원을 조회한다_성공")
	void findMemberByEmail_success() {
		// given
		Member mockMember = Member.create()
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.joinedAt(LocalDateTime.now())
			.name("testA")
			.oauthInformation(new OauthInformation("123234234", OauthType.GOOGLE))
			.profileImage("testImg")
			.build();

		given(memberRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(mockMember));

		// when
		MemberInvitationResponse response = memberService.findMember(mockMember.getEmail());

		// then
		assertSoftly(softly -> {
			softly.assertThat(response.getName()).isEqualTo(mockMember.getName());
			softly.assertThat(response.getProfileImage()).isEqualTo(mockMember.getProfileImage());
		});

	}

	@Test
	@DisplayName("존재하지 않는 회원의 이메일로 회원을 조회한다_실패")
	void findMemberByEmailWhenNotExistsMember_fail() {
		// given
		given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> memberService.findMember("test@test.com"))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());
	}

}
