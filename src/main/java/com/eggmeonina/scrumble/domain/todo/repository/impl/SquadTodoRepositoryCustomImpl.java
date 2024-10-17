package com.eggmeonina.scrumble.domain.todo.repository.impl;


import static com.eggmeonina.scrumble.domain.todo.domain.QSquadToDo.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.todo.dto.QSquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepositoryCustom;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SquadTodoRepositoryCustomImpl implements SquadTodoRepositoryCustom {

	private final JPAQueryFactory query;

	public List<SquadTodoResponse> findSquadTodos(Long squadId, Long memberId, SquadTodoRequest request){
		return query.select(
			new QSquadTodoResponse(squadToDo.toDo.id, squadToDo.id, squadToDo.toDo.contents, squadToDo.toDo.toDoAt, squadToDo.toDo.toDoStatus)
			)
			.from(squadToDo)
			.join(squadToDo.toDo)
			.where(squadToDo.toDo.id.lt(request.getLastToDoId())
				.and(squadToDo.squad.id.eq(squadId))
				.and(squadToDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.member.id.eq(memberId))
				.and(squadToDo.toDo.toDoAt.between(request.getStartDate(), request.getEndDate()))
				.and(squadToDo.toDo.deletedFlag.eq(false)))
			.limit(request.getPageSize())
			.orderBy(squadToDo.toDo.id.asc(), squadToDo.toDo.toDoAt.desc())
			.fetch();
	}
}
