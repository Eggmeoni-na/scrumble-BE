package com.eggmeonina.scrumble.domain.todo.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.todo.dto.ToDoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoResponse;

public interface ToDoRepositoryCustom {
	List<ToDoResponse> findAll(Long memberId, ToDoRequest request);
}
