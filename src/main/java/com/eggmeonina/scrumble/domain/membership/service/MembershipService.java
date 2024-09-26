package com.eggmeonina.scrumble.domain.membership.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.MembershipException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.membership.domain.Membership;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipRole;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipStatus;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.membership.repository.MembershipRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipService {

	private final MemberRepository memberRepository;
	private final MembershipRepository membershipRepository;
	private final SquadRepository squadRepository;

	@Transactional
	public Long createMembership(Long memberId, Long squadId){
		Member foundMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new MembershipException(MEMBER_OR_SQUAD_NOT_FOUND));

		Membership newMembership = Membership.create()
			.member(foundMember)
			.squad(foundSquad)
			.membershipStatus(MembershipStatus.JOIN)
			.membershipRole(MembershipRole.LEADER)
			.build();
		membershipRepository.save(newMembership);
		return newMembership.getId();
	}

	public List<SquadResponse> findBySquads(Long memberId){
		return membershipRepository.findSquads(memberId);
	}

	@Transactional
	public void updateSquad(Long memberId, Long squadId, SquadUpdateRequest request){
		Squad foundSquad = squadRepository.findById(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));
		Membership foundMembership = membershipRepository.findByMemberIdAndSquadId(memberId, squadId)
			.orElseThrow(() -> new MembershipException(MEMBERSHIP_NOT_FOUND));
		// 스쿼드 리더가 아니면 예외를 발생시킨다.
		if(!foundMembership.isLeader()){
			throw new MembershipException(UNAUTHORIZED_ACTION);
		}
		foundSquad.rename(request.getSquadName());
	}
}
