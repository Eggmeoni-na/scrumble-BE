package com.eggmeonina.scrumble.domain.member.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class MemberServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("로그인 요청 시 조회한 회원이 없으면 회원가입 후 회원을 반환한다")
	void login_join_success_returnsMember() {
		// given
		MemberInformation request = new MemberInformation("123456789", "test@naver.com", "testName", "");

		// when
		LoginMember loginMember = memberService.login(request, OauthType.GOOGLE);

		Member foundMember = memberRepository.findByOauthId(request.getOauthId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(loginMember.getMemberId()).isEqualTo(foundMember.getId());
			softly.assertThat(loginMember.getEmail()).isEqualTo(foundMember.getEmail());
		});
	}

	@Test
	@DisplayName("로그인 요청 시 조회한 회원이 있으면 회원을 반환한다_성공")
	void login_success_returnsMember() {
		// given
		MemberInformation request = new MemberInformation("123456789", "test@naver.com", "testName", "");

		memberRepository.save(MemberInformation.to(request, OauthType.GOOGLE));

		// when
		LoginMember loginMember = memberService.login(request, OauthType.GOOGLE);

		Member foundMember = memberRepository.findByOauthId(request.getOauthId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(loginMember.getMemberId()).isEqualTo(foundMember.getId());
			softly.assertThat(loginMember.getEmail()).isEqualTo(foundMember.getEmail());
		});
	}

	@Test
	@DisplayName("회원을 조회한다_성공")
	void findMember_success_returnsMemberResponse() {
		// given
		MemberInformation request = new MemberInformation("123456789", "test@naver.com", "testName", "");

		Member newMember = MemberInformation.to(request, OauthType.GOOGLE);
		memberRepository.save(newMember);

		// when
		MemberResponse response = memberService.findMember(newMember.getId());

		// then
		assertThat(response.getOauthType()).isEqualTo(newMember.getOauthInformation().getOauthType());
		assertThat(response.getEmail()).isEqualTo(newMember.getEmail());
	}

	@Test
	@DisplayName("회원 조회 시, 존재하지 않아 예외가 발생한다_실패")
	void findMember_fail_throwsException() {
		// when
		assertThatThrownBy(() ->memberService.findMember(1L))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("회원을 탈퇴한다_성공")
	void withdraw_success() {
		// given
		MemberInformation request = new MemberInformation("123456789", "test@naver.com", "testName", "");

		Member newMember = MemberInformation.to(request, OauthType.GOOGLE);
		memberRepository.save(newMember);

		// when
		memberService.withdraw(newMember.getId());

		Member foundMember = memberRepository.findById(newMember.getId()).get();

		// then
		assertThat(foundMember.getMemberStatus()).isEqualTo(MemberStatus.WITHDRAW);
	}

	@Test
	@DisplayName("존재하지 않는 회원을 탈퇴 요청하면 예외가 발생한다_실패")
	void withdraw_fail_throwsException() {
		assertThatThrownBy(()-> memberService.withdraw(1L))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

}
