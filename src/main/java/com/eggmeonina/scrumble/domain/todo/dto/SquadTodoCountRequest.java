package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record SquadTodoCountRequest(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {

}
