package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadTodoResponse {
	private Long toDoId;
	private Long squadToDoId;
	private String contents;
	private LocalDate toDoAt;
	private ToDoStatus toDoStatus;
	private Long categoryId;
	private String categoryName;
	private String categoryColor;

	@QueryProjection
	public SquadTodoResponse(Long toDoId, Long squadToDoId, String contents, LocalDate toDoAt, ToDoStatus toDoStatus,
		Long categoryId,
		String categoryName,
		String categoryColor) {
		this.toDoId = toDoId;
		this.squadToDoId = squadToDoId;
		this.contents = contents;
		this.toDoAt = toDoAt;
		this.toDoStatus = toDoStatus;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryColor = categoryColor;
	}
}
