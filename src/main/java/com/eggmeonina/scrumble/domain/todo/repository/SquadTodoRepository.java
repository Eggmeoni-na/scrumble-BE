package com.eggmeonina.scrumble.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;

@Repository
public interface SquadTodoRepository extends JpaRepository<SquadToDo, Long>, SquadTodoRepositoryCustom {
}
