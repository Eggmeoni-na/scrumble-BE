package com.eggmeonina.scrumble.domain.squadmember.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.squadmember.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadMemberService;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SquadMemberFacadeService {

	private final SquadService squadService;
	private final SquadMemberService squadMemberService;

	@Transactional
	public Long createSquad(Long memberId, SquadCreateRequest request){
		// 스쿼드를 생성한다.
		Long squadId = squadService.createSquad(request);
		// 스쿼드장을 등록한다.
		squadMemberService.createMembership(memberId, squadId);
		return squadId;
	}
}
