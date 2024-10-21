package com.eggmeonina.scrumble.domain.todo.facade;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoCommandResponse;
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
	public ToDoCommandResponse createToDoAndSquadToDo(Long squadId, Long memberId, SquadTodoCreateRequest request){
		ToDoCommandResponse response = toDoService.createToDo(memberId, request);
		squadTodoService.createSquadToDo(squadId, response.getToDoId());
		return response;
	}

	@Transactional
	public void deleteToDoAndSquadToDo(Long squadId, Long toDoId, Long memberId){
		// 투두 작성자를 확인한다.
		if(!toDoService.isWriter(memberId, toDoId)){
			throw new ToDoException(WRITER_IS_NOT_MATCH);
		}

		// 스쿼드 투두를 삭제한다.
		squadTodoService.deleteSquadToDo(squadId, toDoId);
		// 자신의 투두를 삭제한다.
		toDoService.deleteToDo(toDoId);
	}
}
