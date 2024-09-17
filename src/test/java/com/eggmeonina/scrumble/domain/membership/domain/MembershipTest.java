package com.eggmeonina.scrumble.domain.membership.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MembershipException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;

class MembershipTest {

	@Test
	@DisplayName("Membership 객체를 생성한다")
	void constructor_success_returnsMembership() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		// when
		Membership newMembership = Membership.create()
			.member(newMember)
			.membershipRole(MembershipRole.LEADER)
			.membershipStatus(MembershipStatus.JOIN)
			.squad(newSquad)
			.build();

		// then
		// 연관관계 편의 메서드가 builder 내부에서 정상 작동하는지 확인한다.
		assertThat(newMembership.getSquad().getSquadName()).isEqualTo(newSquad.getSquadName());
		assertThat(newMembership.getMember().getName()).isEqualTo(newMember.getName());
	}

	@Test
	@DisplayName("Membership 객체 생성 시 Member 객체를 누락하면 예외가 발생한다")
	void constructorWithoutMember_fail_throwsException() {
		// given
		Squad newSquad = new Squad("test group", false);

		// when, then
		assertThatThrownBy(
			Membership.create()
				.membershipRole(MembershipRole.LEADER)
				.membershipStatus(MembershipStatus.JOIN)
				.squad(newSquad)::build)
			.isInstanceOf(MembershipException.class)
			.hasMessageContaining(ErrorCode.MEMBER_OR_GROUP_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("Membership 객체 생성 시 Group 객체를 누락하면 예외가 발생한다")
	void constructorWithoutGroup_fail_throwsException() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());

		// when, then
		assertThatThrownBy(
			Membership.create()
				.membershipRole(MembershipRole.LEADER)
				.membershipStatus(MembershipStatus.JOIN)
				.member(newMember)::build)
			.isInstanceOf(MembershipException.class)
			.hasMessageContaining(ErrorCode.MEMBER_OR_GROUP_NOT_FOUND.getMessage());
	}

}
