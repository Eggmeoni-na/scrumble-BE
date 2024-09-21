package com.eggmeonina.scrumble.domain.membership.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SquadService {

	private final SquadRepository squadRepository;

	@Transactional
	public Long createSquad(SquadCreateRequest request){
		Squad newSquad = squadRepository.save(SquadCreateRequest.to(request));
		return newSquad.getId();
	}
}
