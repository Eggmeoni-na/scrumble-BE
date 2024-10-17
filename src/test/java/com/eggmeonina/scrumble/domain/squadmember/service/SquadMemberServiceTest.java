package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadMemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
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
	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("스쿼드명을 변경한다_성공")
	void updateSquad_success_doNothing() {
		// given
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");
		Member newMember = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember = createSquadMember(newSquad, newMember, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

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
		assertThatThrownBy(() -> squadMemberService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("속하지 않은 회원이 스쿼드명을 변경한다_실패")
	void updateSquadWhenMemberNotInSquad_fail_throwsException() {
		// given
		Squad newSquad = createSquad("테스트 스쿼드");
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong())).willReturn(Optional.empty());

		// when
		assertThatThrownBy(() -> squadMemberService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("리더가 아닌 회원이 스쿼드명을 변경한다_실패")
	void updateSquadWhenNotLeader_fail_throwsException() {
		// given
		Member newMember = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember newSquadMember = createSquadMember(newSquad, newMember, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);
		SquadUpdateRequest request = new SquadUpdateRequest("새로운 스쿼드명");

		given(squadRepository.findById(anyLong())).willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newSquadMember));

		// when
		assertThatThrownBy(() -> squadMemberService.updateSquad(1L, 1L, request))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("리더를 위임한다_정상")
	void assignLeader_success_doNothing() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Member newMember = createMember("test2@test.com", "testB", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember leader = createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);

		SquadMember member = createSquadMember(newSquad, newMember, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

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
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember member1 = createSquadMember(newSquad, newLeader, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(member1));

		// when, then
		assertThatThrownBy(() -> squadMemberService.assignLeader(1L, 1L, 2L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 멤버에게 리더를 위임한다_실패")
	void assignLeaderWhenNotExistMember_fail_throwsMembershipException() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember member1 = createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(member1))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> squadMemberService.assignLeader(1L, 1L, 2L))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("일반 멤버가 스쿼드를 탈퇴한다_성공")
	void leaveSquad_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadMember = createSquadMember(newSquad, newLeader, SquadMemberRole.NORMAL,
			SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadMember));

		// when
		squadMemberService.leaveSquad(1L, 1L);

		// then
		assert squadMember != null;
		assertThat(squadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
	}

	@Test
	@DisplayName("리더가 멤버가 없는 스쿼드를 탈퇴한다_성공")
	void leaveSquadWhenMemberNotInSquad_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadMember = createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadMember));
		given(squadMemberRepository.existsBySquadMemberNotMemberId(anyLong(), anyLong()))
			.willReturn(false);

		// when
		squadMemberService.leaveSquad(1L, 1L);

		// then
		assert squadMember != null;
		assertThat(squadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
	}

	@Test
	@DisplayName("리더가 멤버가 있는 스쿼드를 탈퇴한다_실패")
	void leaveSquadWhenMemberInSquad_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadMember = createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER,
			SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadMember));
		given(squadMemberRepository.existsBySquadMemberNotMemberId(anyLong(), anyLong()))
			.willReturn(true);

		// when
		assertThatThrownBy(() -> squadMemberService.leaveSquad(1L, 1L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(LEADER_CANNOT_LEAVE.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 스쿼드 멤버가 탈퇴 요청한다_실패")
	void leaveSquadWhenMemberNotInSquad_fail() {
		// given
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.empty());
		// when, then
		assertThatThrownBy(() -> squadMemberService.leaveSquad(1L, 1L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("리더가 스쿼드 멤버를 강퇴시킨다_정상")
	void kickSquadMember_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);
		Member newMember = createMember("testB@test.com", "testB", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);
		SquadMember squadMember =
			createSquadMember(newSquad, newMember, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader))
			.willReturn(Optional.ofNullable(squadMember));

		// when
		squadMemberService.kickSquadMember(1L, 1L, 2L);

		// then
		assert squadMember != null;
		assertThat(squadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
	}

	@Test
	@DisplayName("권한 없는 멤버가 스쿼드 멤버를 강퇴시킨다_실패")
	void kickSquadMemberWhenHasNoAuthorization_fail() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);
		Member newMember = createMember("testB@test.com", "testB", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);
		SquadMember squadMember =
			createSquadMember(newSquad, newMember, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader))
			.willReturn(Optional.ofNullable(squadMember));
		// when, then
		assertThatThrownBy(() -> squadMemberService.kickSquadMember(1L, 1L, 2L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 스쿼드 멤버를 강퇴시킨다_실패")
	void kickSquadMemberWhenNotExistsMemberInSquad_fail() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);

		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader))
			.willReturn(Optional.empty());
		// when, then
		assertThatThrownBy(() -> squadMemberService.kickSquadMember(1L, 1L, 2L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드를 삭제한다_정상_1명")
	void deleteSquadWithOneMember_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);

		List<SquadMember> squadMembers = createSquadMembers(squadLeader);

		given(squadRepository.findById(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader));
		given(squadMemberRepository.findBySquadId(anyLong()))
			.willReturn(squadMembers);

		// when
		squadMemberService.deleteSquad(1L, 1L);

		// then
		assert squadLeader != null;
		assertThat(squadLeader.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
		assert newSquad != null;
		assertThat(newSquad.isDeletedFlag()).isTrue();
	}

	@Test
	@DisplayName("스쿼드를 삭제한다_정상_N명")
	void deleteSquadWithMembers_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);
		Member newMember = createMember("test2@test.com", "testB", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.LEADER, SquadMemberStatus.JOIN);
		SquadMember squadMember =
			createSquadMember(newSquad, newMember, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		List<SquadMember> squadMembers = createSquadMembers(squadLeader, squadMember);

		given(squadRepository.findById(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader));
		given(squadMemberRepository.findBySquadId(anyLong()))
			.willReturn(squadMembers);

		// when
		squadMemberService.deleteSquad(1L, 1L);

		// then
		assertSoftly(softly -> {
			softly.assertThat(squadLeader.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(squadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.LEAVE);
			softly.assertThat(newSquad.isDeletedFlag()).isTrue();
		});
	}

	@Test
	@DisplayName("권한 없는 멤버가 스쿼드를 삭제한다_실패")
	void deleteSquadWithHasNoAuthorization_fail() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadLeader =
			createSquadMember(newSquad, newLeader, SquadMemberRole.NORMAL, SquadMemberStatus.JOIN);

		given(squadRepository.findById(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));
		given(squadMemberRepository.findByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadLeader));

		// when, then
		assertThatThrownBy(() -> squadMemberService.deleteSquad(1L, 1L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(UNAUTHORIZED_ACTION.getMessage());
	}

	@Test
	@DisplayName("이미 존재하는 스쿼드 멤버를 생성한다_실패")
	void createSquadMemberWhenExistsMemberInSquad_fail() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		given(squadRepository.findById(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));
		given(memberRepository.findById(anyLong()))
			.willReturn(Optional.ofNullable(newLeader));
		given(squadMemberRepository.existsByMemberIdAndSquadId(anyLong(), anyLong()))
			.willReturn(true);

		// when, then
		assertThatThrownBy(()->squadMemberService.inviteSquadMember(1L, 1L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(DUPLICATE_SQUADMEMBER.getMessage());
	}

	@Test
	@DisplayName("회원을 존재하지 않는 스쿼드에 초대한다_실패")
	void inviteSquadMemberWhenNotExistsSquad_fail() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newLeader));
		given(squadRepository.findById(anyLong())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()->squadMemberService.inviteSquadMember(1L, 1L))
			.isInstanceOf(SquadMemberException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 회원을 스쿼드에 초대한다_실패")
	void inviteSquadMemberWhenNotExistsMember_fail() {
		// given
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()->squadMemberService.inviteSquadMember(1L, 1L))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드 멤버 초대에 수락한다_성공")
	void acceptInvitation_success() {
		// given
		Member newLeader = createMember("test@test.com", "testA", MemberStatus.JOIN);

		Squad newSquad = createSquad("테스트 스쿼드");

		SquadMember squadMember =
			createSquadMember(newSquad, newLeader, SquadMemberRole.NORMAL, SquadMemberStatus.INVITING);

		given(squadRepository.findByIdAndDeletedFlagNot(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));

		given(squadMemberRepository.findByMemberIdAndSquadIdWithInvitingStatus(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(squadMember));

		// when
		squadMemberService.responseInvitation(1L, 1L, SquadMemberStatus.JOIN);

		// then
		assertThat(squadMember.getSquadMemberStatus()).isEqualTo(SquadMemberStatus.JOIN);
	}

	@Test
	@DisplayName("스쿼드 초대하지 않은 회원이 응답한다_실패")
	void responseInvitationWhenDoNotInvitedMember_success() {
		// given
		Squad newSquad = createSquad("테스트 스쿼드");

		given(squadRepository.findByIdAndDeletedFlagNot(anyLong()))
			.willReturn(Optional.ofNullable(newSquad));

		given(squadMemberRepository.findByMemberIdAndSquadIdWithInvitingStatus(anyLong(), anyLong()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()->squadMemberService.responseInvitation(1L, 1L, SquadMemberStatus.JOIN))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUADMEMBER_NOT_INVITED.getMessage());
	}

	@Test
	@DisplayName("스쿼드 초대하지 않은 회원이 응답한다_실패")
	void responseInvitationWhenNotExistsSquad_success() {
		// given
		given(squadRepository.findByIdAndDeletedFlagNot(anyLong()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()->squadMemberService.responseInvitation(1L, 1L, SquadMemberStatus.JOIN))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

}
