package com.eggmeonina.scrumble.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eggmeonina.scrumble.domain.todo.domain.ToDo;

public interface TodoRepository extends JpaRepository<ToDo, Long> {
}
