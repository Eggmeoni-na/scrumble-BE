package com.eggmeonina.scrumble.domain.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eggmeonina.scrumble.domain.todo.domain.ToDo;

public interface TodoRepository extends JpaRepository<ToDo, Long> {

	@Query("""
		SELECT t
		  FROM ToDo t
		 WHERE t.id = :toDoId AND t.deletedFlag = false
		""")
	Optional<ToDo> findByIdAndDeletedFlagNot(Long toDoId);
}
