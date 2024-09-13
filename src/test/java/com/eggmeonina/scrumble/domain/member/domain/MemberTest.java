package com.eggmeonina.scrumble.domain.member.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

class MemberTest {

	@Test
	@DisplayName("회원 탈퇴한다")
	void withdraw_success() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation("oauthId", OauthType.GOOGLE))
			.build();

		// when
		newMember.withdraw();

		// then
		assertThat(newMember.getMemberStatus()).isEqualTo(MemberStatus.WITHDRAW);
		assertThat(newMember.getLeavedAt()).isNotNull();
	}

	@Test
	@DisplayName("탈퇴한 회원이 탈퇴 요청 시 예외가 발생한다")
	void withdraw_fail_throwsException() {
		// given
		Member withdrawMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.WITHDRAW)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation("oauthId", OauthType.GOOGLE))
			.build();

		// when
		assertThatThrownBy(withdrawMember::withdraw)
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_ALREADY_WITHDRAW.getMessage());
	}
}
