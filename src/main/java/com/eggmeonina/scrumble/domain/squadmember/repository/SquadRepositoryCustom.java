package com.eggmeonina.scrumble.domain.squadmember.repository;

import java.util.Optional;

import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;

public interface SquadRepositoryCustom {

	Optional<SquadDetailResponse> findSquadAndMembers(Long squadId);
}
