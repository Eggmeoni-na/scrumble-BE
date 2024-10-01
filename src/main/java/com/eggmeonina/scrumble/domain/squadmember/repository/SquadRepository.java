package com.eggmeonina.scrumble.domain.squadmember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long>, SquadRepositoryCustom {

	@Query("""
		SELECT s
		  FROM Squad s
		 WHERE s.id = :squadId AND s.deletedFlag = false
		""")
	Optional<Squad> findByIdAndDeletedFlagNot(Long squadId);
}
