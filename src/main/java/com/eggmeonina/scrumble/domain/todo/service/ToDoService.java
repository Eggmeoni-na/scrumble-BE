package com.eggmeonina.scrumble.domain.todo.service;

import org.springframework.stereotype.Service;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {

	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;

	public Long createToDo(Long memberId, SquadTodoCreateRequest request){
		Member foundMember = memberRepository.findByIdAndMemberStatusNotJOIN(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		ToDo newToDo = SquadTodoCreateRequest.to(request, foundMember);
		todoRepository.save(newToDo);
		return newToDo.getId();
	}
}
