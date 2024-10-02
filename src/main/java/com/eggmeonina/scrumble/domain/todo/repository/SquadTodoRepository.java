package com.eggmeonina.scrumble.domain.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;

@Repository
public interface SquadTodoRepository extends JpaRepository<SquadToDo, Long>, SquadTodoRepositoryCustom {

	@Query("""
		SELECT sq
		  FROM SquadToDo sq
		 WHERE sq.toDo.id = :toDoId AND sq.squad.id = :squadId AND sq.deletedFlag = false
		""")
	Optional<SquadToDo> findByToDoIdAndSquadIdAndDeletedFlagNot(Long toDoId, Long squadId);
}
