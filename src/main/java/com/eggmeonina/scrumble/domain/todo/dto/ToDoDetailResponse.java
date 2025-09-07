package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
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
	private ToDoStatus toDoStatus;
	private Long categoryId;
	private String categoryName;
	private String categoryColor;

	@QueryProjection
	public ToDoDetailResponse(Long toDoId, Long squadId, String squadName, String contents, LocalDate todoAt,
		ToDoStatus toDoStatus, Long categoryId, String categoryName, String categoryColor) {
		this.toDoId = toDoId;
		this.squadId = squadId;
		this.squadName = squadName;
		this.contents = contents;
		this.todoAt = todoAt;
		this.toDoStatus = toDoStatus;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryColor = categoryColor;
	}
}
