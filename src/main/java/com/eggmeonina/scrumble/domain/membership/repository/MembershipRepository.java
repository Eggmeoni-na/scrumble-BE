package com.eggmeonina.scrumble.domain.membership.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.membership.domain.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long>, MembershipRepositoryCustom {

	@Query("""
      SELECT m
        FROM Membership m
       WHERE m.member.id = :memberId AND m.squad.id = :squadId AND m.membershipStatus = 'JOIN'
      """)
	Optional<Membership> findByMemberIdAndSquadId(final Long memberId, final Long squadId);
}
