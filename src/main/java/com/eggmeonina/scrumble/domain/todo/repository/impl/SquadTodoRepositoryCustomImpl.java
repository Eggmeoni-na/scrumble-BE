package com.eggmeonina.scrumble.domain.todo.repository.impl;

import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquadMember.*;
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

	public List<SquadTodoResponse> findSquadTodos(Long squadMemberId, SquadTodoRequest request) {
		return query.select(
				new QSquadTodoResponse(squadToDo.toDo.id, squadToDo.id, squadToDo.toDo.contents, squadToDo.toDo.toDoAt,
					squadToDo.toDo.toDoStatus)
			)
			.from(squadMember)
			.join(squadToDo)
			.on(squadMember.squad.id.eq(squadToDo.squad.id)
				.and(squadMember.member.id.eq(squadToDo.toDo.member.id)))
			.where(squadMember.id.eq(squadMemberId)
				.and(squadToDo.toDo.id.gt(request.getLastToDoId()))
				.and(squadToDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.toDoAt.between(request.getStartDate(), request.getEndDate()))
				.and(squadToDo.toDo.deletedFlag.eq(false)))
			.limit(request.getPageSize())
			.orderBy(squadToDo.toDo.toDoAt.desc(), squadToDo.toDo.id.asc())
			.fetch();
	}
}
