package com.eggmeonina.scrumble.domain.squadmember.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;

public interface SquadMemberRepositoryCustom {

	List<SquadResponse> findSquads(final Long memberId);

	boolean existsBySquadMemberNotMemberId(final Long squadId, final Long memberId);

	boolean existsByMemberIdAndSquadIdAndSquadMemberStatus(final Long memberId, final Long squadId, final SquadMemberStatus squadMemberStatus);
}
