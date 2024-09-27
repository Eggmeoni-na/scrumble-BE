package com.eggmeonina.scrumble.domain.squadmember.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;

class SquadMemberTest {

	@Test
	@DisplayName("Membership 객체를 생성한다")
	void constructor_success_returnsMembership() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		// when
		SquadMember newSquadMember = SquadMember.create()
			.member(newMember)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squad(newSquad)
			.build();

		// then
		assertThat(newSquadMember.getSquad().getSquadName()).isEqualTo(newSquad.getSquadName());
		assertThat(newSquadMember.getMember().getName()).isEqualTo(newMember.getName());
	}

	@Test
	@DisplayName("Membership 객체 생성 시 Member 객체를 누락하면 예외가 발생한다")
	void constructorWithoutMember_fail_throwsException() {
		// given
		Squad newSquad = new Squad("test group", false);

		// when, then
		assertThatThrownBy(
			SquadMember.create()
				.squadMemberRole(SquadMemberRole.LEADER)
				.squadMemberStatus(SquadMemberStatus.JOIN)
				.squad(newSquad)::build)
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(MEMBER_OR_SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("Membership 객체 생성 시 Group 객체를 누락하면 예외가 발생한다")
	void constructorWithoutGroup_fail_throwsException() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());

		// when, then
		assertThatThrownBy(
			SquadMember.create()
				.squadMemberRole(SquadMemberRole.LEADER)
				.squadMemberStatus(SquadMemberStatus.JOIN)
				.member(newMember)::build)
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(MEMBER_OR_SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("리더인지 확인한다_true")
	void isLeader_success_returnTrue() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		// when
		SquadMember newSquadMember = SquadMember.create()
			.member(newMember)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squad(newSquad)
			.build();
		// when
		boolean isLeader = newSquadMember.isLeader();

		// then
		assertThat(isLeader).isTrue();
	}

	@Test
	@DisplayName("리더인지 확인한다_false")
	void isLeader_success_returnFalse() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		SquadMember newSquadMember = SquadMember.create()
			.member(newMember)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squad(newSquad)
			.build();

		// when
		boolean isLeader = newSquadMember.isLeader();

		// then
		assertThat(isLeader).isFalse();
	}

	@Test
	@DisplayName("리더로 위임한다_정상")
	void assignLeader_success() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		SquadMember newMembership = SquadMember.create()
			.member(newMember)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squad(newSquad)
			.build();
		
		// when
		newMembership.assignLeader();

		// then
		assertThat(newMembership.isLeader()).isTrue();
	}

	@Test
	@DisplayName("리더에서 사임한다_정상")
	void resignAsLeader_success() {
		// given
		Member newMember = new Member("test@test.com", "test", "", new OauthInformation("1234", OauthType.GOOGLE),
			MemberStatus.JOIN, LocalDateTime.now());
		Squad newSquad = new Squad("test group", false);

		SquadMember newMembership = SquadMember.create()
			.member(newMember)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squad(newSquad)
			.build();

		// when
		newMembership.resignAsLeader();

		// then
		assertThat(newMembership.isLeader()).isFalse();
	}

}
