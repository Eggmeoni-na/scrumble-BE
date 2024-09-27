package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SquadMemberService {

	private final MemberRepository memberRepository;
	private final SquadMemberRepository squadMemberRepository;
	private final SquadRepository squadRepository;

	@Transactional
	public Long createMembership(Long memberId, Long squadId){
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

	public List<SquadResponse> findBySquads(Long memberId){
		return squadMemberRepository.findSquads(memberId);
	}

	@Transactional
	public void updateSquad(Long memberId, Long squadId, SquadUpdateRequest request){
		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));
		SquadMember foundSquadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		// 스쿼드 리더가 아니면 예외를 발생시킨다.
		hasAuthorization(foundSquadMember);
		foundSquad.rename(request.getSquadName());
	}

	@Transactional
	public void assignLeader(Long squadId, Long leaderId, Long newLeaderId){
		SquadMember foundLeader = squadMemberRepository.findByMemberIdAndSquadId(leaderId, squadId)
			.orElseThrow(() -> new SquadException(SQUADMEMBER_NOT_FOUND));
		// 스쿼드 리더가 아니면 예외를 발생시킨다.
		hasAuthorization(foundLeader);
		SquadMember foundMember = squadMemberRepository.findByMemberIdAndSquadId(newLeaderId, squadId)
			.orElseThrow(() -> new SquadException(SQUADMEMBER_NOT_FOUND));
		foundLeader.resignAsLeader();
		foundMember.assignLeader();
	}

	@Transactional
	public void leaveSquad(Long squadId, Long memberId){
		SquadMember foundSquadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		// 스쿼드에 인원이 있는 스쿼드 리더는 위임하거나 삭제만 가능하다.
		if(foundSquadMember.isLeader() && squadMemberRepository.existsBySquadMemberNotMemberId(squadId, memberId)){
			throw new SquadMemberException(LEADER_CANNOT_LEAVE);
		}
		foundSquadMember.leave();
	}

	@Transactional
	public void kickSquadMember(Long squadId, Long leaderId, Long memberId){
		// 리더가 권한을 가졌는지 확인한다.
		SquadMember squadLeader = squadMemberRepository.findByMemberIdAndSquadId(leaderId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		hasAuthorization(squadLeader);

		SquadMember squadMember = squadMemberRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));

		// 멤버를 탈퇴시킨다.
		squadMember.leave();
	}

	private void hasAuthorization(SquadMember squadMember){
		if(!squadMember.isLeader()){
			throw new SquadMemberException(UNAUTHORIZED_ACTION);
		}
	}
}
