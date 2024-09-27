package com.eggmeonina.scrumble.domain.squadmember.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SquadService {

	private final SquadRepository squadRepository;

	@Transactional
	public Long createSquad(SquadCreateRequest request){
		Squad newSquad = squadRepository.save(SquadCreateRequest.to(request));
		return newSquad.getId();
	}

	public SquadDetailResponse findSquadAndMembers(Long squadId){
		return squadRepository.findSquadAndMembers(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));
	}
}
