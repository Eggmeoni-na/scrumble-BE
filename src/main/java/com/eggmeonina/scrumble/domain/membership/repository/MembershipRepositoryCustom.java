package com.eggmeonina.scrumble.domain.membership.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.membership.dto.SquadResponse;

public interface MembershipRepositoryCustom {

	List<SquadResponse> findSquads(Long memberId);
}
