package com.eggmeonina.scrumble.domain.squadmember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;

@Repository
public interface SquadMemberRepository extends JpaRepository<SquadMember,Long>, SquadMemberRepositoryCustom {

	@Query("""
      SELECT s
        FROM SquadMember s
       WHERE s.member.id = :memberId AND s.squad.id = :squadId AND s.squadMemberStatus = 'JOIN'
      """)
	Optional<SquadMember> findByMemberIdAndSquadId(final Long memberId, final Long squadId);

	@Query("""
	 SELECT s
	   FROM SquadMember s
	  WHERE s.squad.id = :squadId AND s.squadMemberStatus = 'JOIN'
	""")
	List<SquadMember> findBySquadId(final Long squadId);
}
