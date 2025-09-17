package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountResponse;
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

	public List<SquadTodoResponse> findSquadTodos(Long squadMemberId, SquadTodoRequest request) {
		return squadTodoRepository.findSquadTodos(squadMemberId, request);
	}

	@Transactional
	public Long createSquadToDo(Long squadId, Long toDoId) {
		Squad foundSquad = squadRepository.findByIdAndDeletedFlagNot(squadId)
			.orElseThrow(() -> new SquadException(SQUAD_NOT_FOUND));
		ToDo foundTodo = todoRepository.findByIdAndDeletedFlagNot(toDoId)
			.orElseThrow(() -> new ToDoException(TODO_NOT_FOUND));

		SquadToDo newSquadToDo = SquadToDo.create()
			.squad(foundSquad)
			.toDo(foundTodo)
			.deletedFlag(false)
			.build();

		squadTodoRepository.save(newSquadToDo);
		return newSquadToDo.getId();
	}

	@Transactional
	public void deleteSquadToDo(Long squadId, Long toDoId){
		SquadToDo foundSquadToDo = squadTodoRepository.findByToDoIdAndSquadIdAndDeletedFlagNot(toDoId, squadId)
			.orElseThrow(() -> new ToDoException(SQUAD_TODO_NOT_FOUND));
		foundSquadToDo.delete();
	}

	@Transactional(readOnly = true)
	public List<SquadTodoCountResponse> getSquadTodoCountSummary(Long memberId, Long squadMemberId, SquadTodoCountRequest request) {
		return squadTodoRepository.getSquadTodoCountSummary(memberId, squadMemberId, request);
	}
}
