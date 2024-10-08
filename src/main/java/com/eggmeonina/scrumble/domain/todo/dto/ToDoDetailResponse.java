package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.TodoStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ToDoDetailResponse {
	private Long toDoId;
	private Long squadId;
	private String squadName;
	private String contents;
	private LocalDate todoAt;
	private TodoStatus todoStatus;

	@QueryProjection
	public ToDoDetailResponse(Long toDoId, Long squadId, String squadName, String contents, LocalDate todoAt,
		TodoStatus todoStatus) {
		this.toDoId = toDoId;
		this.squadId = squadId;
		this.squadName = squadName;
		this.contents = contents;
		this.todoAt = todoAt;
		this.todoStatus = todoStatus;
	}
}
