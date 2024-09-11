package com.eggmeonina.scrumble.domain.member.service;

import static org.assertj.core.api.SoftAssertions.*;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("로그인 요청 시 조회한 회원이 없으면 회원가입 후 회원을 반환한다.")
	void login_success_returnsMember() {
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

}
