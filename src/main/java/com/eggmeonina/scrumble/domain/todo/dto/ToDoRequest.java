package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToDoRequest {
	@NotNull
	private LocalDate startDate;
	@NotNull
	private LocalDate endDate;
	@NotNull
	private String toDoType;
	@NotNull
	private Long lastToDoId;
	@Positive
	private long pageSize;
}
