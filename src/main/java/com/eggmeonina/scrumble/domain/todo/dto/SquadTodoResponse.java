package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.TodoStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadTodoResponse {
	private Long toDoId;
	private Long squadToDoId;
	private String contents;
	private LocalDate todoAt;
	private TodoStatus todoStatus;

	@QueryProjection
	public SquadTodoResponse(Long toDoId, Long squadToDoId, String contents, LocalDate todoAt, TodoStatus todoStatus) {
		this.toDoId = toDoId;
		this.squadToDoId = squadToDoId;
		this.contents = contents;
		this.todoAt = todoAt;
		this.todoStatus = todoStatus;
	}
}
