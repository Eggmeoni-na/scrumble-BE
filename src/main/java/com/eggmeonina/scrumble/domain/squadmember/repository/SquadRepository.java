package com.eggmeonina.scrumble.domain.squadmember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long>, SquadRepositoryCustom {
}
