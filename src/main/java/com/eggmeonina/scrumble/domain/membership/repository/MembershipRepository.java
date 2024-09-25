package com.eggmeonina.scrumble.domain.membership.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.membership.domain.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long>, MembershipRepositoryCustom {

	Optional<Membership> findByMemberIdAndSquadId(final Long memberId, final Long squadId);
}
