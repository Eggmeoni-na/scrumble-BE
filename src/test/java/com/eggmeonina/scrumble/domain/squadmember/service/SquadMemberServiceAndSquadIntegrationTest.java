package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class SquadMemberServiceAndSquadIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private SquadMemberService squadMemberService;

	@Autowired
	private SquadService squadService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SquadMemberRepository squadMemberRepository;

	@Autowired
	private SquadRepository squadRepository;

	@Test
	@DisplayName("회원과 스쿼드로 멤버십을 생성한다")
	void createMembership_success_returnsMembershipId() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long membershipId =
			squadMemberService.createMembership(newMember.getId(), newSquad.getId());

		SquadMember foundSquadMember = squadMemberRepository.findById(membershipId).get();

		// then
		assertThat(membershipId).isEqualTo(foundSquadMember.getId());
	}

	@Test
	@DisplayName("회원 없이 스쿼드만으로 멤버십을 생성하면 예외가 발생한다")
	void createMembershipWithoutMember_fail_throwsException() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long squadId = newSquad.getId();
		assertThatThrownBy(() -> squadMemberService.createMembership(0L, squadId))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드 없이 회원만으로 멤버십을 생성하면 예외가 발생한다")
	void createMembershipWithoutSquad_fail_throwsException() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long memberId = newMember.getId();
		assertThatThrownBy(() -> squadMemberService.createMembership(memberId, 0L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("나의 스쿼드들을 조회한다")
	void findSquads_success_returnSquads() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember = SquadMember.create()
			.member(newMember)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		memberRepository.save(newMember);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember);

		// when
		List<SquadResponse> squads = squadMemberService.findBySquads(newMember.getId());

		// then
		assertSoftly(softly -> {
			softly.assertThat(squads).hasSize(1);
			softly.assertThat(squads.get(0).getSquadId()).isEqualTo(newSquad.getId());
			softly.assertThat(squads.get(0).getSquadName()).isEqualTo(newSquad.getSquadName());
		});
	}

	@Test
	@DisplayName("나의 스쿼드들을 조회한다_0개")
	void findBySquads_empty_returnEmptyList() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		memberRepository.save(newMember);

		// when
		List<SquadResponse> squads = squadMemberService.findBySquads(newMember.getId());

		// then
		assertThat(squads).isEmpty();
	}

	@Test
	@DisplayName("스쿼드를 조회한다_성공")
	void findSquadAndMembers_success_returnSquadDetailResponse() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Member newMember2 = Member.create()
			.name("testB")
			.email("test2@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("987654321", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember newSquadMember2 = SquadMember.create()
			.member(newMember2)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newSquad.getId());

		// then
		assertSoftly(softly -> {
			softly.assertThat(response.getSquadId()).isEqualTo(newSquad.getId());
			softly.assertThat(response.getSquadName()).isEqualTo(newSquad.getSquadName());
			softly.assertThat(response.getSquadMembers().get(0).getMemberId()).isEqualTo(newMember1.getId());
			softly.assertThat(response.getSquadMembers().get(0).getName()).isEqualTo(newMember1.getName());
			softly.assertThat(response.getSquadMembers().get(0).getProfileImg()).isEqualTo(newMember1.getProfileImage());
			softly.assertThat(response.getSquadMembers()).hasSize(2);
		});
	}
	@Test
	@DisplayName("삭제된 스쿼드를 조회한다_실패")
	void findSquadAndMembers_fail_throwsSquadException() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(true)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.LEAVE)
			.build();

		memberRepository.save(newMember1);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);

		// when
		assertThatThrownBy(()->squadService.findSquadAndMembers(newSquad.getId()))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드를 탈퇴한 멤버가 있는 스쿼드를 조회한다_성공")
	void findSquadAndMembersWithWithdrawMemberFromSquad_success_returnSquadDetailResponse() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Member newMember2 = Member.create()
			.name("testB")
			.email("test2@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("987654321", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember newSquadMember2 = SquadMember.create()
			.member(newMember2)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.LEAVE)
			.build();

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newSquad.getId());

		// then
		assertThat(response.getSquadMembers()).hasSize(1);
	}

	@Test
	@DisplayName("회원 탈퇴한 멤버가 있는 스쿼드를 조회한다_성공")
	void findSquadWithWithdrawnMember_success_returnSquadDetailResponse() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Member newMember2 = Member.create()
			.name("testB")
			.email("test2@test.com")
			.memberStatus(MemberStatus.WITHDRAW)
			.oauthInformation(new OauthInformation("987654321", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember newSquadMember2 = SquadMember.create()
			.member(newMember2)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.LEAVE)
			.build();

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newSquad.getId());

		// then
		assertThat(response.getSquadMembers()).hasSize(1);
	}

	@Test
	@DisplayName("리더가 멤버가 없는 스쿼드를 탈퇴한다_성공")
	void leaveSquadWhenMemberNotInSquad_success() {
		// given
		Member newMember = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember = SquadMember.create()
			.member(newMember)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		memberRepository.save(newMember);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember);

		// when
		squadMemberService.leaveSquad(newSquad.getId(), newMember.getId());

		SquadMember foundMember = squadMemberRepository.findById(newSquadMember.getId()).get();

		// then
		assertThat(foundMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
	}

	@Test
	@DisplayName("리더가 탈퇴한 멤버만 있는 스쿼드를 탈퇴한다_성공")
	void leaveSquadWhenLeavedMemberInSquad_success() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Member newMember2 = Member.create()
			.name("testB")
			.email("test2@test.com")
			.memberStatus(MemberStatus.WITHDRAW)
			.oauthInformation(new OauthInformation("987654321", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember newSquadMember2 = SquadMember.create()
			.member(newMember2)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.LEAVE)
			.build();

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		squadMemberService.leaveSquad(newSquad.getId(), newMember1.getId());

		SquadMember foundMember = squadMemberRepository.findById(newSquadMember1.getId()).get();

		// then
		assertThat(foundMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
	}

	@Test
	@DisplayName("리더가 멤버가 있는 스쿼드를 탈퇴한다_실패")
	void leaveSquadWhenMemberInSquad_fail() {
		// given
		Member newMember1 = Member.create()
			.name("testA")
			.email("test@test.com")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456789", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Member newMember2 = Member.create()
			.name("testB")
			.email("test2@test.com")
			.memberStatus(MemberStatus.WITHDRAW)
			.oauthInformation(new OauthInformation("987654321", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		SquadMember newSquadMember1 = SquadMember.create()
			.member(newMember1)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember newSquadMember2 = SquadMember.create()
			.member(newMember2)
			.squad(newSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		assertThatThrownBy(() ->squadMemberService.leaveSquad(newSquad.getId(), newMember1.getId()))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(LEADER_CANNOT_LEAVE.getMessage())
			;
	}
}
