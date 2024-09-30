package com.eggmeonina.scrumble.domain.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SquadTodoService {

	private final SquadTodoRepository squadTodoRepository;

	public List<SquadTodoResponse> findSquadTodos(Long squadId, Long memberId, SquadTodoRequest request){
		return squadTodoRepository.findSquadTodos(squadId, memberId, request);
	}
}
