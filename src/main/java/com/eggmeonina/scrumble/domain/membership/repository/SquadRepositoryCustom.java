package com.eggmeonina.scrumble.domain.membership.repository;

import java.util.Optional;

import com.eggmeonina.scrumble.domain.membership.dto.SquadDetailResponse;

public interface SquadRepositoryCustom {

	Optional<SquadDetailResponse> findSquadAndMembers(Long squadId);
}
