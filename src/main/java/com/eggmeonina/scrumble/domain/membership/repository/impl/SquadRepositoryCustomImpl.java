package com.eggmeonina.scrumble.domain.membership.repository.impl;

import static com.eggmeonina.scrumble.domain.member.domain.QMember.*;
import static com.eggmeonina.scrumble.domain.membership.domain.QMembership.*;
import static com.eggmeonina.scrumble.domain.membership.domain.QSquad.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.membership.domain.MembershipStatus;
import com.eggmeonina.scrumble.domain.membership.dto.QSquadDetailResponse;
import com.eggmeonina.scrumble.domain.membership.dto.QSquadMemberResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SquadRepositoryCustomImpl implements SquadRepositoryCustom {

	private final JPAQueryFactory query;

	@Override
	public Optional<SquadDetailResponse> findSquadAndMembers(Long squadId) {
		List<SquadDetailResponse> response = query.selectFrom(squad)
			.where(squad.id.eq(squadId)
				.and(squad.deletedFlag.eq(false)))
			.join(membership)
			.on(membership.squad.id.eq(squad.id)
				.and(membership.membershipStatus.eq(MembershipStatus.JOIN)))
			.join(member)
			.on(membership.member.id.eq(member.id)
				.and(member.memberStatus.eq(MemberStatus.JOIN)))
			.transform(
				groupBy(squad.id).list(
					new QSquadDetailResponse(squad.id, squad.squadName,
						list(
							new QSquadMemberResponse(member.id, member.profileImage, member.name)
						))
				)
			);
		if (response.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(response.get(0));
	}
}
