package com.eggmeonina.scrumble.domain.todo.dto;

import java.time.LocalDate;

import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoCommandResponse {
	private Long toDoId;
	private String contents;
	private LocalDate toDoAt;
	private ToDoStatus toDoStatus;

	public static ToDoCommandResponse to(ToDo toDo){
		return new ToDoCommandResponse(toDo.getId(), toDo.getContents(), toDo.getToDoAt(), toDo.getToDoStatus());
	}
}
