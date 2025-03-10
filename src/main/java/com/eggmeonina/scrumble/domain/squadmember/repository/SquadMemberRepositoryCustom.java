package com.eggmeonina.scrumble.domain.squadmember.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;

public interface SquadMemberRepositoryCustom {

	List<SquadResponse> findSquads(final Long memberId, SquadMemberRole role);

	boolean existsBySquadMemberNotMemberId(final Long squadId, final Long memberId);

	boolean existsByMemberIdAndSquadId(final Long memberId, final Long squadId);
}
