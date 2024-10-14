package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ToDoResponse {
	private LocalDate toDoAt;
	private List<ToDoDetailResponse> toDoDetails;

	@QueryProjection
	public ToDoResponse(LocalDate toDoAt, List<ToDoDetailResponse> toDoDetails) {
		this.toDoAt = toDoAt;
		this.toDoDetails = toDoDetails;
	}
}
