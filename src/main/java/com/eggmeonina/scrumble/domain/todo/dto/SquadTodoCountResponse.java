package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadTodoCountResponse {
	private LocalDate todoAt;
	private Long totalCount;
	private Long completedCount;

	@QueryProjection
	public SquadTodoCountResponse(LocalDate todoAt, Long totalCount, Long completedCount){
		this.todoAt = todoAt;
		this.totalCount = totalCount;
		this.completedCount = completedCount;
	}

}
