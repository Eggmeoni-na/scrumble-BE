package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "스쿼드 멤버 투두 조회 요청 DTO")
@AllArgsConstructor
public class SquadTodoRequest {
	@NotNull
	@Schema(description = "시작일자")
	private LocalDate startDate;
	@NotNull
	@Schema(description = "종료일자")
	private LocalDate endDate;
	@NotNull
	@Schema(description = "투두 타입")
	private String toDoType;
	@NotNull
	@Schema(description = "마지막 조회 toDoId, 최초는 99999999")
	private Long lastToDoId;
	@Positive
	private long pageSize;
}
