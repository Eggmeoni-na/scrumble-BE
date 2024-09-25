package com.eggmeonina.scrumble.domain.membership.repository.impl;

import static com.eggmeonina.scrumble.domain.membership.domain.QMembership.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.membership.domain.MembershipStatus;
import com.eggmeonina.scrumble.domain.membership.dto.QSquadResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.membership.repository.MembershipRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepositoryCustom {

	private final JPAQueryFactory query;

	@Override
	public List<SquadResponse> findSquads(Long memberId) {
		// TODO : createAt desc 기준으로 index 생성하기
		return
			query.select(
					new QSquadResponse(membership.squad.id, membership.squad.squadName))
				.from(membership)
				.join(membership.squad)
				.where(membership.member.id.eq(memberId).
					and(membership.membershipStatus.eq(MembershipStatus.JOIN)).
					and(membership.squad.deletedFlag.eq(false))
				)
				.fetch();
	}
}
