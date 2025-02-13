package com.eggmeonina.scrumble.domain.member.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWithdrawService {

	private final MemberRepository memberRepository;
	private final SquadRepository squadRepository;
	private final SquadMemberRepository squadMemberRepository;

	/**
	 * 회원을 탈퇴한다.
	 * 단, 리더인 스쿼드가 있는 경우
	 * 팀원이 없으면 스쿼드 탈퇴, 스쿼드 삭제, 회원 탈퇴를 한다.
	 * 팀원이 있으면 예외를 발생시키고 회원 탈퇴하지 못한다.
	 *
	 * @param memberId
	 */
	@Transactional
	public void withdraw(Long memberId) {
		Member foundMember = memberRepository.findByIdAndMemberStatusNotJOIN(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 스쿼드들을 조회한다.
		List<SquadMember> squadMembers = squadMemberRepository.findByMemberIdWithJOIN(memberId);

		// 스쿼드의 리더일 때, 회원이 존재하는 스쿼드면 throw 발생시킨다.
		for (SquadMember squadMember : squadMembers) {
			if (squadMember.isLeader() && squadMemberRepository.existsBySquadMemberNotMemberId(
				squadMember.getSquad().getId(), memberId)) {
				throw new ExpectedException(ErrorCode.LEADER_CANNOT_LEAVE);
			}
		}
		List<Long> squadMemberIdList = squadMembers.stream()
			.map(SquadMember::getId)
			.toList();

		List<Long> squadIdList = squadMembers.stream()
			.filter(SquadMember::isLeader)
			.map(squadMember -> squadMember.getSquad().getId())
			.toList();

		// 스쿼드를 탈퇴한다.
		if (!squadMemberIdList.isEmpty()) {
			squadMemberRepository.bulkLeaveSquadMember(squadMemberIdList, LocalDateTime.now());
		}
		// 리더인 스쿼드를 삭제한다.
		if (!squadIdList.isEmpty()) {
			squadRepository.bulkDeleteSquads(squadIdList, LocalDateTime.now());
		}

		foundMember.withdraw();
	}
}
