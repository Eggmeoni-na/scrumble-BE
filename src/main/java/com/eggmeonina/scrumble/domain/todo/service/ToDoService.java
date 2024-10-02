package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoUpdateRequest;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ToDoService {

	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;

	@Transactional
	public Long createToDo(Long memberId, SquadTodoCreateRequest request){
		Member foundMember = memberRepository.findByIdAndMemberStatusNotJOIN(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		ToDo newToDo = SquadTodoCreateRequest.to(request, foundMember);
		todoRepository.save(newToDo);
		return newToDo.getId();
	}

	public boolean isWriter(Long memberId, Long toDoId){
		return todoRepository.existsByIdAndMemberId(toDoId, memberId);
	}

	@Transactional
	public Long deleteToDo(Long toDoId){
		ToDo foundToDo = todoRepository.findByIdAndDeletedFlagNot(toDoId)
			.orElseThrow(() -> new ToDoException(TODO_NOT_FOUND));
		foundToDo.delete();
		return foundToDo.getId();
	}

	@Transactional
	public void updateToDo(Long memberId, Long toDoId, ToDoUpdateRequest request){
		if(!isWriter(memberId, toDoId)){
			throw new ToDoException(WRITER_IS_NOT_MATCH);
		}
		ToDo foundToDo = todoRepository.findByIdAndDeletedFlagNot(toDoId)
			.orElseThrow(() -> new ToDoException(TODO_NOT_FOUND));
		foundToDo.update(request.getContents(), request.getToDoStatus(), request.getToDoAt());
	}
}
