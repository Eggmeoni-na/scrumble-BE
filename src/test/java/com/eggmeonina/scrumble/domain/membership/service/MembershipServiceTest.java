package com.eggmeonina.scrumble.domain.membership.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.MembershipException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.membership.domain.Membership;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipRole;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipStatus;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.membership.repository.MembershipRepository;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

	@InjectMocks
	private MembershipService membershipService;
	@Mock
	private MembershipRepository membershipRepository;
	@Mock
	private SquadRepository squadRepository;


	@Test
	@DisplayName("스쿼드명을 변경한다_성공")
	void updateSquad_success_doNothing() {
		// given
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");
		Member newMember = Member.create()
			.email("test@test.com")
			.name("testA")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("1234567", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		Membership newMembership = Membership.create()
			.squad(newSquad)
			.member(newMember)
			.membershipRole(MembershipRole.LEADER)
			.membershipStatus(MembershipStatus.JOIN)
			.build();

		given(membershipRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newMembership));
		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));

		// when
		membershipService.updateSquad(1L, 1L, request);

		// then
		assert newSquad != null;
		assertThat(newSquad.getSquadName()).isEqualTo(request.getSquadName());
	}

	@Test
	@DisplayName("존재하지 않는 스쿼드명을 변경한다_실패")
	void updateSquad_fail_throwsException() {
		// given
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.empty());

		// when
		assertThatThrownBy(()-> membershipService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("속하지 않은 회원이 스쿼드명을 변경한다_실패")
	void updateSquadWhenMemberNotInSquad_fail_throwsException() {
		// given
		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));
		given(membershipRepository.findByMemberIdAndSquadId(anyLong(), anyLong())).willReturn(Optional.empty());

		// when
		assertThatThrownBy(()-> membershipService.updateSquad(1L, 1L, request))
			.isInstanceOf(MembershipException.class)
			.hasMessageContaining(MEMBERSHIP_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("리더가 아닌 회원이 스쿼드명을 변경한다_실패")
	void updateSquadWhenNotLeader_fail_throwsException() {
		// given
		Member newMember = Member.create()
			.email("test@test.com")
			.name("testA")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("1234567", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		Membership newMembership = Membership.create()
			.squad(newSquad)
			.member(newMember)
			.membershipRole(MembershipRole.NORMAL)
			.membershipStatus(MembershipStatus.JOIN)
			.build();
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));
		given(membershipRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newMembership));

		// when
		assertThatThrownBy(()-> membershipService.updateSquad(1L, 1L, request))
			.isInstanceOf(MembershipException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

}
