package com.eggmeonina.scrumble.domain.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.membership.domain.Squad;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
}