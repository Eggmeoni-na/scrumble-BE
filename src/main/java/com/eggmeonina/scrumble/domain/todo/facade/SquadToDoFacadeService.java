package com.eggmeonina.scrumble.domain.todo.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.service.SquadTodoService;
import com.eggmeonina.scrumble.domain.todo.service.ToDoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SquadToDoFacadeService {

	private final ToDoService toDoService;
	private final SquadTodoService squadTodoService;

	/**
	 * 투두를 생성할 때, 스쿼드 투두도 같이 생성한다.
	 * @param squadId
	 * @param memberId
	 * @param request
	 * @return todoId
	 */
	@Transactional
	public Long createToDoAndSquadToDo(Long squadId, Long memberId, SquadTodoCreateRequest request){
		Long toDoId = toDoService.createToDo(memberId, request);
		squadTodoService.createSquadToDo(squadId, toDoId);
		return toDoId;
	}
}
