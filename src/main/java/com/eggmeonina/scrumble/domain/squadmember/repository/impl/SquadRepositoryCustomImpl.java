package com.eggmeonina.scrumble.domain.squadmember.repository.impl;

import static com.eggmeonina.scrumble.domain.member.domain.QMember.*;
import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquad.*;
import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquadMember.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.dto.QSquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.QSquadMemberResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepositoryCustom;
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
			.join(squadMember)
			.on(squadMember.squad.id.eq(squad.id)
				.and(squadMember.squadMemberStatus.eq(SquadMemberStatus.JOIN)))
			.join(member)
			.on(squadMember.member.id.eq(member.id)
				.and(member.memberStatus.eq(MemberStatus.JOIN)))
			.transform(
				groupBy(squad.id).list(
					new QSquadDetailResponse(squad.id, squad.squadName,
						list(
							new QSquadMemberResponse(member.id, squadMember.id, member.profileImage, member.name, squadMember.squadMemberRole)
						))
				)
			);
		if (response.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(response.get(0));
	}
}
