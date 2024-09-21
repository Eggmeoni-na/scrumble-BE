package com.eggmeonina.scrumble.domain.membership.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.MembershipException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.membership.domain.Membership;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipRole;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipStatus;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
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
}
