package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadMemberResponse;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SquadService {

	private final SquadRepository squadRepository;

	@Transactional
	public Long createSquad(SquadCreateRequest request) {
		Squad newSquad = squadRepository.save(SquadCreateRequest.to(request));
		return newSquad.getId();
	}

	public SquadDetailResponse findSquadAndMembers(Long memberId, Long squadId) {
		// 스쿼드와 스쿼드에 속한 멤버들을 조회한다.
		SquadDetailResponse response = squadRepository.findSquadAndMembers(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));

		// 나의 스쿼드멤버 롤을 조회한다. 없으면 스쿼드에 속한 회원이 아니라 예외처리한다.
		SquadMemberRole mySquadMemberRole =
			response.getSquadMembers().stream()
				.filter(squadMember -> squadMember.getMemberId().equals(memberId))
				.map(SquadMemberResponse::getSquadMemberRole)
				.findFirst()
				.orElseThrow(() -> new SquadMemberException(SQUADMEMBER_NOT_FOUND));
		return new SquadDetailResponse(mySquadMemberRole, response);
	}
}
