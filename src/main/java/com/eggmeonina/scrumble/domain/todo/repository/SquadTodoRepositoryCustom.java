package com.eggmeonina.scrumble.domain.todo.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;

public interface SquadTodoRepositoryCustom {

	List<SquadTodoResponse> findSquadTodos(Long squadMemberId, SquadTodoRequest request);
}
