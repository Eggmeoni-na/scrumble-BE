package com.eggmeonina.scrumble.domain.todo.repository.impl;

import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquad.*;
import static com.eggmeonina.scrumble.domain.todo.domain.QSquadToDo.*;
import static com.eggmeonina.scrumble.domain.todo.domain.QToDo.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.todo.dto.QToDoDetailResponse;
import com.eggmeonina.scrumble.domain.todo.dto.QToDoResponse;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.ToDoRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ToDoRepositoryCustomImpl implements ToDoRepositoryCustom {

	private final JPAQueryFactory query;

	@Override
	public List<ToDoResponse> findAll(Long memberId, ToDoRequest request) {
		return query.selectFrom(toDo)
			.join(toDo.member)
			.join(squadToDo)
			.on(squadToDo.toDo.id.eq(toDo.id)
				.and(squadToDo.deletedFlag.eq(false)))
			.join(squad)
			.on(squad.id.eq(squadToDo.squad.id))
			.where(toDo.id.lt(request.getLastToDoId())
			.and(toDo.member.id.eq(memberId))
				.and(toDo.todoAt.between(request.getStartDate(), request.getEndDate()))
				.and(toDo.deletedFlag.eq(false)))
			.orderBy(toDo.todoAt.desc(), toDo.id.desc())
			.transform(
				groupBy(toDo.todoAt).list(
					new QToDoResponse(toDo.todoAt, list(
						new QToDoDetailResponse(toDo.id, squad.id, squad.squadName, toDo.contents, toDo.todoAt, toDo.todoStatus)
					))
				)
			);
	}
}