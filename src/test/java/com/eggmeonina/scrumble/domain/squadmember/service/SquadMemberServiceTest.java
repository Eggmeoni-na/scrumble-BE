package com.eggmeonina.scrumble.domain.squadmember.service;

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

import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

@ExtendWith(MockitoExtension.class)
class SquadMemberServiceTest {

	@InjectMocks
	private SquadMemberService squadMemberService;
	@Mock
	private SquadMemberRepository squadMemberRepository;
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

		SquadMember newSquadMember = SquadMember.create()
			.squad(newSquad)
			.member(newMember)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newSquadMember));
		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));

		// when
		squadMemberService.updateSquad(1L, 1L, request);

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
		assertThatThrownBy(()-> squadMemberService.updateSquad(1L, 1L, request))
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
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong())).willReturn(Optional.empty());

		// when
		assertThatThrownBy(()-> squadMemberService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
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

		SquadMember newSquadMember = SquadMember.create()
			.squad(newSquad)
			.member(newMember)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newSquadMember));

		// when
		assertThatThrownBy(()-> squadMemberService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("리더를 위임한다_정상")
	void assignLeader_success_doNothing() {
		// given
		Member newLeader = Member.create()
			.email("test@test.com")
			.name("testA")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("1234567", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

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

		SquadMember leader = SquadMember.create()
			.squad(newSquad)
			.member(newLeader)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		SquadMember member = SquadMember.create()
			.squad(newSquad)
			.member(newMember)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(leader))
			.willReturn(Optional.ofNullable(member));

		// when
		squadMemberService.assignLeader(1L, 1L, 2L);

		// then
		assert leader != null;
		assertThat(leader.isLeader()).isFalse();
		assert member != null;
		assertThat(member.isLeader()).isTrue();
	}

	@Test
	@DisplayName("리더가 아닌 멤버가 리더를 위임한다_실패")
	void assignLeader_fail_throwsMembershipException() {
		// given
		// given
		Member newLeader = Member.create()
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

		SquadMember member1 = SquadMember.create()
			.squad(newSquad)
			.member(newLeader)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(member1));
		
		// when, then
		assertThatThrownBy(() ->squadMemberService.assignLeader(1L, 1L, 2L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 멤버에게 리더를 위임한다_실패")
	void assignLeaderWhenNotExistMember_fail_throwsMembershipException() {
		// given
		// given
		Member newLeader = Member.create()
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

		SquadMember member1 = SquadMember.create()
			.squad(newSquad)
			.member(newLeader)
			.squadMemberRole(SquadMemberRole.LEADER)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(member1))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> squadMemberService.assignLeader(1L, 1L, 2L))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
	}

}
