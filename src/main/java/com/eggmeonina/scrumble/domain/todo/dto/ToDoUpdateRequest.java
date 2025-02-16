package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ToDo 수정 정보를 담은 객체")
public class ToDoUpdateRequest {
	@NotBlank
	@Schema(description = "ToDo 내용")
	private String contents;
	@NotNull
	@Schema(description = "ToDo 상태 - PENDING(미완료), COMPLETED(완료)")
	private ToDoStatus toDoStatus;
	@NotNull
	@PastOrPresent
	@Schema(description = "변경하고자 하는 ToDo 일자 (변경 없으면 기존 일자)")
	private LocalDate toDoAt;
}
