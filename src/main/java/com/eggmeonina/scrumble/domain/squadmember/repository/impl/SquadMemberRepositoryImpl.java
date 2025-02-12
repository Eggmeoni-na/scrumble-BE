package com.eggmeonina.scrumble.domain.squadmember.repository.impl;

import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquadMember.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.dto.QSquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SquadMemberRepositoryImpl implements SquadMemberRepositoryCustom {

	private final JPAQueryFactory query;

	@Override
	public List<SquadResponse> findSquads(Long memberId, SquadMemberRole role) {
		// TODO : createAt desc 기준으로 index 생성하기
		return
			query.select(
					new QSquadResponse(squadMember.squad.id, squadMember.squad.squadName))
				.from(squadMember)
				.join(squadMember.squad)
				.where(squadMember.member.id.eq(memberId).
					and(squadMember.squadMemberStatus.eq(SquadMemberStatus.JOIN)).
					and(squadMember.squad.deletedFlag.eq(false)).
					and(eqRole(role))
				)
				.fetch();
	}

	private BooleanExpression eqRole(SquadMemberRole role) {
		if (role == null) {
			return null;
		}
		return squadMember.squadMemberRole.eq(role);
	}

	@Override
	public boolean existsBySquadMemberNotMemberId(Long squadId, Long memberId) {
		Integer fetchOne = query.selectOne()
			.from(squadMember)
			.where(squadMember.member.id.ne(memberId)
				.and(squadMember.squad.id.eq(squadId))
				.and(squadMember.squadMemberStatus.eq(SquadMemberStatus.JOIN)))
			.fetchFirst();
		return fetchOne != null;
	}

	@Override
	public boolean existsByMemberIdAndSquadId(Long memberId, Long squadId) {
		Integer fetchOne = query.selectOne()
			.from(squadMember)
			.where(squadMember.member.id.eq(memberId)
				.and(squadMember.squad.id.eq(squadId))
				.and(squadMember.squadMemberStatus.eq(SquadMemberStatus.JOIN)
					.or(squadMember.squadMemberStatus.eq(SquadMemberStatus.INVITING))))
			.fetchFirst();
		return fetchOne != null;
	}
}
