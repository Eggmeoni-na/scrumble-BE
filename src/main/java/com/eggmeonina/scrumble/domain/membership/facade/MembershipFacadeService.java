package com.eggmeonina.scrumble.domain.membership.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.service.MembershipService;
import com.eggmeonina.scrumble.domain.membership.service.SquadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipFacadeService {

	private final SquadService squadService;
	private final MembershipService membershipService;

	@Transactional
	public Long createSquad(Long memberId, SquadCreateRequest request){
		// 스쿼드를 생성한다.
		Long squadId = squadService.createSquad(request);
		// 스쿼드장을 등록한다.
		membershipService.createMembership(memberId, squadId);
		return squadId;
	}
}
