package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SquadTodoRequest {
	private LocalDate startDate;
	private LocalDate endDate;
}
