package com.eggmeonina.scrumble.domain.todo.repository;

import java.util.List;

import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountResponse;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;

public interface SquadTodoRepositoryCustom {

	List<SquadTodoResponse> findSquadTodos(Long squadMemberId, SquadTodoRequest request);

	List<SquadTodoCountResponse> getSquadTodoCountSummary(Long memberId, Long squadMemberId, SquadTodoCountRequest request);
}
