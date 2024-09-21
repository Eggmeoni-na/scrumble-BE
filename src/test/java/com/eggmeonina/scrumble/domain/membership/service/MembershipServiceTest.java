package com.eggmeonina.scrumble.domain.membership.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.MembershipException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.membership.domain.Membership;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.repository.MembershipRepository;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class MembershipServiceTest extends IntegrationTestHelper {

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MembershipRepository membershipRepository;

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
			membershipService.createMembership(newMember.getId(), newSquad.getId());

		Membership foundMembership = membershipRepository.findById(membershipId).get();

		// then
		assertThat(membershipId).isEqualTo(foundMembership.getId());
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
		assertThatThrownBy(()-> membershipService.createMembership(0L, squadId))
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
		assertThatThrownBy(() -> membershipService.createMembership(memberId, 0L))
			.isInstanceOf(MembershipException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

}
