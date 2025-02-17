package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.squadmember.event.InvitedEvent;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SquadMemberService {

	private final MemberRepository memberRepository;
	private final SquadMemberRepository squadMemberRepository;
	private final SquadRepository squadRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public Long createSquadMember(Long memberId, Long squadId) {
		Member foundMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadMemberException(MEMBER_OR_SQUAD_NOT_FOUND));

		SquadMember newSquadMember = SquadMember.create()
			.member(foundMember)
			.squad(foundSquad)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squadMemberRole(SquadMemberRole.LEADER)
			.build();
		squadMemberRepository.save(newSquadMember);
		return newSquadMember.getId();
	}

	@Transactional
	public Long inviteSquadMember(String memberName, Long invitedMemberId, Long squadId) {
		Member foundMember = memberRepository.findById(invitedMemberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadMemberException(SQUAD_NOT_FOUND));

		SquadMember newSquadMember = SquadMember.create()
			.member(foundMember)
			.squad(foundSquad)
			.squadMemberStatus(SquadMemberStatus.INVITING)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.build();

		// 스쿼드에 존재하는 회원이면 예외를 발생시킨다.
		if (squadMemberRepository.existsByMemberIdAndSquadId(invitedMemberId, squadId)) {
			throw new SquadMemberException(DUPLICATE_SQUADMEMBER);
		}

		squadMemberRepository.save(newSquadMember);

		// 이벤트를 발행한다.
		eventPublisher.publishEvent(
			new InvitedEvent(foundMember.getId(), memberName, foundSquad.getId(), foundSquad.getSquadName(),
				NotificationType.INVITE_REQUEST));
		return newSquadMember.getId();
	}

	public List<SquadResponse> findSquads(Long memberId, String role) {
		if(role == null || role.isEmpty()){
			return squadMemberRepository.findSquads(memberId, null);
		}
		return squadMemberRepository.findSquads(memberId, SquadMemberRole.valueOf(role));
	}

	@Transactional
	public void updateSquad(Long memberId, Long squadId, SquadUpdateRequest request) {
		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));
		SquadMember foundSquadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		// 스쿼드 리더가 아니면 예외를 발생시킨다.
		hasAuthorization(foundSquadMember);
		foundSquad.rename(request.getSquadName());
	}

	@Transactional
	public void assignLeader(Long squadId, Long leaderId, Long newLeaderId) {
		SquadMember foundLeader = squadMemberRepository.findByMemberIdAndSquadId(leaderId, squadId)
			.orElseThrow(() -> new SquadException(SQUADMEMBER_NOT_FOUND));
		// 스쿼드 리더가 아니면 예외를 발생시킨다.
		hasAuthorization(foundLeader);
		SquadMember foundMember = squadMemberRepository.findByMemberIdAndSquadId(newLeaderId, squadId)
			.orElseThrow(() -> new SquadException(SQUADMEMBER_NOT_FOUND));
		foundLeader.resignAsLeader();
		foundMember.assignLeader();
	}

	/**
	 * 스쿼드 탈퇴
	 * 일반 스쿼드원 - 탈퇴 가능
	 * 리더 - 팀원이 없으면 탈퇴/스쿼드 삭제
	 *       팀원이 있으면 탈퇴 불가
	 * @param squadId
	 * @param memberId
	 */
	@Transactional
	public void leaveSquad(Long squadId, Long memberId) {

		Squad foundSquad = squadRepository.findByIdAndDeletedFlagNot(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));

		SquadMember foundSquadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));

		boolean isLeader = foundSquadMember.isLeader();
		boolean hasOtherMembers = squadMemberRepository.existsBySquadMemberNotMemberId(squadId, memberId);

		// 멤버 있는 스쿼드 리더는 탈퇴가 불가능하다.
		// 스쿼드 위임 및 삭제만 가능하다.
		if (isLeader && hasOtherMembers) {
			throw new SquadMemberException(LEADER_CANNOT_LEAVE);
		}

		// 스쿼드 리더가 탈퇴 시 스쿼드는 실시간 삭제된다.
		if(isLeader){
			foundSquad.delete();
		}

		foundSquadMember.leave();
	}

	@Transactional
	public void kickSquadMember(Long squadId, Long leaderId, Long memberId) {
		// 리더가 권한을 가졌는지 확인한다.
		SquadMember squadLeader = squadMemberRepository.findByMemberIdAndSquadId(leaderId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		hasAuthorization(squadLeader);

		SquadMember squadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));

		// 멤버를 탈퇴시킨다.
		squadMember.leave();
	}

	@Transactional
	public void deleteSquad(Long squadId, Long leaderId) {
		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));

		// 리더가 권한을 가졌는지 확인한다.
		SquadMember squadLeader = squadMemberRepository.findByMemberIdAndSquadId(leaderId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		hasAuthorization(squadLeader);

		// 스쿼드 멤버들을 모두 탈퇴 시킨다.
		squadMemberRepository.findBySquadId(squadId).forEach(SquadMember::leave);
		foundSquad.delete();
	}

	/**
	 * 멤버 초대 응답
	 *
	 * @param squadId
	 * @param memberId
	 * @param squadMemberStatus
	 */
	@Transactional
	public void responseInvitation(Long squadId, Long memberId, SquadMemberStatus squadMemberStatus) {
		squadRepository.findByIdAndDeletedFlagNot(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));

		SquadMember foundSquadMember = squadMemberRepository.findByMemberIdAndSquadIdWithInvitingStatus(memberId,
				squadId)
			.orElseThrow(() -> new SquadException(SQUADMEMBER_NOT_INVITED));

		foundSquadMember.responseInvitation(squadMemberStatus);
	}

	private void hasAuthorization(SquadMember squadMember) {
		if (!squadMember.isLeader()) {
			throw new SquadMemberException(UNAUTHORIZED_ACTION);
		}
	}
}
