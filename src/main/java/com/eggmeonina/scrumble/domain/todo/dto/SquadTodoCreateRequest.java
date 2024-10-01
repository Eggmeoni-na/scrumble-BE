package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.domain.TodoStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquadTodoCreateRequest {
	private ToDoType toDoType;
	private String contents;
	private LocalDate todoAt;
	public static ToDo to(SquadTodoCreateRequest request, Member member){
		return ToDo.create()
			.toDoType(request.getToDoType())
			.contents(request.getContents())
			.todoStatus(TodoStatus.PENDING)
			.todoAt(request.getTodoAt())
			.deletedFlag(false)
			.member(member)
			.build();
	}
}
