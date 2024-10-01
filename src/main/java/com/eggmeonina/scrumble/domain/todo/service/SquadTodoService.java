package com.eggmeonina.scrumble.domain.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SquadTodoService {

	private final TodoRepository todoRepository;
	private final SquadTodoRepository squadTodoRepository;
	private final SquadRepository squadRepository;

	public List<SquadTodoResponse> findSquadTodos(Long squadId, Long memberId, SquadTodoRequest request) {
		return squadTodoRepository.findSquadTodos(squadId, memberId, request);
	}

	@Transactional
	public Long createSquadToDo(Long squadId, Long toDoId) {
		Squad foundSquad = squadRepository.findByIdAndDeletedFlagNot(squadId)
			.orElseThrow(() -> new SquadException(ErrorCode.SQUAD_NOT_FOUND));
		ToDo foundTodo = todoRepository.findByIdAndDeletedFlagNot(toDoId)
			.orElseThrow(() -> new ToDoException(ErrorCode.TODO_NOT_FOUND));

		SquadToDo newSquadToDo = SquadToDo.create()
			.squad(foundSquad)
			.toDo(foundTodo)
			.deletedFlag(false)
			.build();

		squadTodoRepository.save(newSquadToDo);
		return newSquadToDo.getId();
	}

}
