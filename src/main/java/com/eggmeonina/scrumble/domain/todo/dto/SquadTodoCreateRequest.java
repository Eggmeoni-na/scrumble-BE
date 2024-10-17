package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquadTodoCreateRequest {
	private ToDoType toDoType;
	private String contents;
	private LocalDate toDoAt;
	public static ToDo to(SquadTodoCreateRequest request, Member member){
		return ToDo.create()
			.toDoType(request.getToDoType())
			.contents(request.getContents())
			.toDoStatus(ToDoStatus.PENDING)
			.toDoAt(request.getToDoAt())
			.deletedFlag(false)
			.member(member)
			.build();
	}
}
