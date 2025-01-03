package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadMemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
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
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long membershipId =
			squadMemberService.createSquadMember(newMember.getId(), newSquad.getId());

		SquadMember foundSquadMember = squadMemberRepository.findById(membershipId).get();

		// then
		assertThat(membershipId).isEqualTo(foundSquadMember.getId());
	}

	@Test
	@DisplayName("회원 없이 스쿼드만으로 멤버십을 생성하면 예외가 발생한다")
	void createMembershipWithoutMember_fail_throwsException() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long squadId = newSquad.getId();
		assertThatThrownBy(() -> squadMemberService.createSquadMember(0L, squadId))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드 없이 회원만으로 멤버십을 생성하면 예외가 발생한다")
	void createMembershipWithoutSquad_fail_throwsException() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		// when
		Long memberId = newMember.getId();
		assertThatThrownBy(() -> squadMemberService.createSquadMember(memberId, 0L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("나의 스쿼드들을 조회한다")
	void findSquads_success_returnSquads() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember = createSquadMember(newSquad, newMember, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

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
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

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
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		SquadMember newSquadMember2 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newMember1.getId(), newSquad.getId());

		// then
		assertSoftly(softly -> {
			softly.assertThat(response.getSquadId()).isEqualTo(newSquad.getId());
			softly.assertThat(response.getSquadName()).isEqualTo(newSquad.getSquadName());
			softly.assertThat(response.getSquadMembers().get(0).getMemberId()).isEqualTo(newMember1.getId());
			softly.assertThat(response.getSquadMembers().get(0).getName()).isEqualTo(newMember1.getName());
			softly.assertThat(response.getSquadMembers().get(0).getProfileImg()).isEqualTo(newMember1.getProfileImage());
			softly.assertThat(response.getSquadMembers().get(0).getSquadMemberRole()).isEqualTo(newSquadMember1.getSquadMemberRole());
			softly.assertThat(response.getSquadMembers()).hasSize(2);
		});
	}

	@Test
	@DisplayName("삭제된 스쿼드를 조회한다_실패")
	void findSquadAndMembers_fail_throwsSquadException() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(true)
			.build();

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.LEAVE);

		memberRepository.save(newMember1);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);

		// when
		assertThatThrownBy(() -> squadService.findSquadAndMembers(newMember1.getId(), newSquad.getId()))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드를 탈퇴한 멤버가 있는 스쿼드를 조회한다_성공")
	void findSquadAndMembersWithWithdrawMemberFromSquad_success_returnSquadDetailResponse() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		SquadMember newSquadMember2 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.LEAVE);

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newMember1.getId(), newSquad.getId());

		// then
		assertThat(response.getSquadMembers()).hasSize(1);
	}

	@Test
	@DisplayName("회원 탈퇴한 멤버가 있는 스쿼드를 조회한다_성공")
	void findSquadWithWithdrawnMember_success_returnSquadDetailResponse() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.WITHDRAW);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		SquadMember newSquadMember2 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.LEAVE);

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		SquadDetailResponse response = squadService.findSquadAndMembers(newMember1.getId(), newSquad.getId());

		// then
		assertThat(response.getSquadMembers()).hasSize(1);
	}

	@Test
	@DisplayName("리더가 멤버가 없는 스쿼드를 탈퇴한다_성공")
	void leaveSquadWhenMemberNotInSquad_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember = createSquadMember(newSquad, newMember, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

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
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.WITHDRAW);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		SquadMember newSquadMember2 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.LEAVE);

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
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.WITHDRAW);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember1 = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		SquadMember newSquadMember2 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);

		memberRepository.save(newMember1);
		memberRepository.save(newMember2);
		squadRepository.save(newSquad);
		squadMemberRepository.save(newSquadMember1);
		squadMemberRepository.save(newSquadMember2);

		// when
		assertThatThrownBy(() -> squadMemberService.leaveSquad(newSquad.getId(), newMember1.getId()))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(LEADER_CANNOT_LEAVE.getMessage())
		;
	}

	@Test
	@DisplayName("스쿼드를 삭제한다_성공_1개")
	void deleteSquadWithOneMember_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newMember, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);
		squadMemberRepository.save(squadLeader);

		// when
		squadMemberService.deleteSquad(newSquad.getId(), newMember.getId());

		SquadMember foundLeader = squadMemberRepository.findById(squadLeader.getId()).get();
		Squad foundSquad = squadRepository.findById(newSquad.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundLeader.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundSquad.isDeletedFlag()).isTrue();
		});
	}

	@Test
	@DisplayName("스쿼드를 삭제한다_성공_N개")
	void deleteSquadWithMembers_success() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);
		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.JOIN);
		Member newMember3 = createMember("testC", "test3@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);
		SquadMember squadMember1 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);
		SquadMember squadMember2 = createSquadMember(newSquad, newMember3, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(newMember1, newMember2, newMember3));
		squadRepository.save(newSquad);
		squadMemberRepository.saveAll(List.of(squadLeader, squadMember1, squadMember2));

		// when
		squadMemberService.deleteSquad(newSquad.getId(), newMember1.getId());

		SquadMember foundLeader = squadMemberRepository.findById(squadLeader.getId()).get();
		SquadMember foundMember1 = squadMemberRepository.findById(squadMember1.getId()).get();
		SquadMember foundMember2 = squadMemberRepository.findById(squadMember2.getId()).get();
		Squad foundSquad = squadRepository.findById(newSquad.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundLeader.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundMember1.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundMember2.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundSquad.isDeletedFlag()).isTrue();
		});
	}

	@Test
	@DisplayName("존재하는 스쿼드 멤버를 스쿼드 멤버로 추가한다_실패")
	void inviteSquadMemberWhenExistsMemberInSquad_fail() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);
		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);
		SquadMember squadMember1 = createSquadMember(newSquad, newMember2, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(newMember1, newMember2));
		squadRepository.save(newSquad);
		squadMemberRepository.saveAll(List.of(squadLeader, squadMember1));

		// when, then
		assertThatThrownBy(()->squadMemberService.inviteSquadMember("testMember", newMember2.getId(), newSquad.getId()))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(DUPLICATE_SQUADMEMBER.getMessage());
	}

	@Test
	@DisplayName("스쿼드 멤버로 추가한다_성공")
	void inviteSquadMember_success() {
		// given
		Member newMember1 = createMember("testA", "test@test.com", MemberStatus.JOIN);
		Member newMember2 = createMember("testB", "test2@test.com", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader = createSquadMember(newSquad, newMember1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(newMember1, newMember2));
		squadRepository.save(newSquad);
		squadMemberRepository.saveAll(List.of(squadLeader));

		// when
		Long squadMemberId = squadMemberService.inviteSquadMember("testMember", newMember2.getId(), newSquad.getId());

		SquadMember foundSquadMember = squadMemberRepository.findById(squadMemberId).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundSquadMember.getSquadMemberRole()).isEqualTo(SquadMemberRole.NORMAL);
			softly.assertThat(foundSquadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.INVITING);
		});

	}
}
