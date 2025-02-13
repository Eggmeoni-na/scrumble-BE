package com.eggmeonina.scrumble.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.fixture.MemberFixture;
import com.eggmeonina.scrumble.fixture.SquadMemberFixture;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class MemberWithdrawServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private MemberWithdrawService memberWithdrawService;

	@Autowired
	private SquadMemberRepository squadMemberRepository;

	@Autowired
	private SquadRepository squadRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원이 탈퇴한다_성공")
	void withdraw_whenNotLeader_success() {
		// given
		Member 테스트유저1 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");
		memberRepository.save(테스트유저1);

		// when
		memberWithdrawService.withdraw(테스트유저1.getId());
		Member foundMember = memberRepository.findById(테스트유저1.getId()).get();

		// // then
		assertThat(foundMember.getMemberStatus()).isEqualTo(MemberStatus.WITHDRAW);
	}

	@Test
	@DisplayName("스쿼드에 팀원이 없는 리더인 회원이 탈퇴한다_성공")
	void withdraw_WhenRoleIsLeader_success1() {
		// given
		Member 테스트유저1 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");

		Squad 테스트스쿼드 = SquadMemberFixture.createSquad("테스트스쿼드");

		SquadMember leader = SquadMemberFixture.createSquadMember(테스트스쿼드, 테스트유저1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(테스트유저1));
		squadRepository.save(테스트스쿼드);
		squadMemberRepository.saveAll(List.of(leader));

		// when
		memberWithdrawService.withdraw(테스트유저1.getId());

		Member foundMember = memberRepository.findById(테스트유저1.getId()).get();
		SquadMember foundSquadMember = squadMemberRepository.findById(leader.getId()).get();
		Squad foundSquad = squadRepository.findById(테스트스쿼드.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundMember.getMemberStatus()).isEqualTo(MemberStatus.WITHDRAW);
			softly.assertThat(foundSquadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundSquad.isDeletedFlag()).isTrue();
		});
	}

	@Test
	@DisplayName("스쿼드에 팀원이 있는 리더인 회원이 탈퇴한다_실패")
	void withdraw_WhenRoleIsLeader_success2() {
		// given
		Member 테스트유저1 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");
		Member 테스트유저2 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");

		Squad 테스트스쿼드 = SquadMemberFixture.createSquad("테스트스쿼드");

		SquadMember leader = SquadMemberFixture.createSquadMember(테스트스쿼드, 테스트유저1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);
		SquadMember normalSquadMember = SquadMemberFixture.createSquadMember(테스트스쿼드, 테스트유저2, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(테스트유저1, 테스트유저2));
		squadRepository.save(테스트스쿼드);
		squadMemberRepository.saveAll(List.of(leader, normalSquadMember));

		// when
		assertThatThrownBy(() -> memberWithdrawService.withdraw(테스트유저1.getId()))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(ErrorCode.LEADER_CANNOT_LEAVE.getMessage());

		Member foundMember = memberRepository.findById(테스트유저1.getId()).get();
		SquadMember foundSquadMember = squadMemberRepository.findById(normalSquadMember.getId()).get();
		Squad foundSquad = squadRepository.findById(테스트스쿼드.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundMember.getMemberStatus()).isEqualTo(MemberStatus.JOIN);
			softly.assertThat(foundSquadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.JOIN);
			softly.assertThat(foundSquad.isDeletedFlag()).isFalse();
		});
	}

	@Test
	@DisplayName("리더가 아닌 회원이 탈퇴한다_성공")
	void withdraw_WhenRoleIsNormal_success() {
		// given
		Member 테스트유저1 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");
		Member 테스트유저2 = MemberFixture.createJOINMember("test@test.com", "테스트유저1", "123234235");

		Squad 테스트스쿼드 = SquadMemberFixture.createSquad("테스트스쿼드");

		SquadMember leader = SquadMemberFixture.createSquadMember(테스트스쿼드, 테스트유저1, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);
		SquadMember normalSquadMember = SquadMemberFixture.createSquadMember(테스트스쿼드, 테스트유저2, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		memberRepository.saveAll(List.of(테스트유저1, 테스트유저2));
		squadRepository.save(테스트스쿼드);
		squadMemberRepository.saveAll(List.of(leader, normalSquadMember));

		// when
		memberWithdrawService.withdraw(테스트유저2.getId());

		Member foundMember = memberRepository.findById(테스트유저2.getId()).get();
		SquadMember foundSquadMember = squadMemberRepository.findById(normalSquadMember.getId()).get();
		Squad foundSquad = squadRepository.findById(테스트스쿼드.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundMember.getMemberStatus()).isEqualTo(MemberStatus.WITHDRAW);
			softly.assertThat(foundSquadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(foundSquad.isDeletedFlag()).isFalse();
		});
	}

}
