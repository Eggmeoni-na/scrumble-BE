package com.eggmeonina.scrumble.domain.member.service;

import static com.eggmeonina.scrumble.fixture.SquadMemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.fixture.MemberFixture;

@ExtendWith(MockitoExtension.class)
class MemberWithdrawServiceTest {

	@InjectMocks
	private MemberWithdrawService memberWithdrawService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private SquadMemberRepository squadMemberRepository;

	@Test
	@DisplayName("회원이 존재하지 않을 때 회원 탈퇴한다_실패")
	void leaveMember_whenNotExistMember_fail() {
		// given
		given(memberRepository.findByIdAndMemberStatusNotJOIN(anyLong()))
			.willReturn(Optional.empty());
		
		// when, then
		assertThatThrownBy(() -> memberWithdrawService.withdraw(1L))
			.hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage())
			.isInstanceOf(ExpectedException.class);
	}

	@Test
	@DisplayName("팀원이 존재하는 스쿼드의 리더가 회원 탈퇴한다_실패")
	void leaveMember_whenSquadLeader_fail() {
		// given
		Member 테스트유저 = MemberFixture.createJOINMember("test@test.com", "테스트 유저", "123345");
		Squad 테스트스쿼드 = createSquad("테스트스쿼드");
		SquadMember 스쿼드멤버1 = createSquadMember(테스트스쿼드, 테스트유저, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);
		List<SquadMember> squadMembers = createSquadMembers(스쿼드멤버1);

		given(memberRepository.findByIdAndMemberStatusNotJOIN(anyLong()))
			.willReturn(Optional.of(테스트유저));
		given(squadMemberRepository.findByMemberIdWithJOIN(anyLong()))
			.willReturn(squadMembers);
		given(squadMemberRepository.existsBySquadMemberNotMemberId(any(), any()))
			.willReturn(Boolean.TRUE);

		// when, then
		assertThatThrownBy(() -> memberWithdrawService.withdraw(1L))
			.hasMessageContaining(ErrorCode.LEADER_CANNOT_LEAVE.getMessage())
			.isInstanceOf(ExpectedException.class);
	}

}
