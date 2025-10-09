package com.eggmeonina.scrumble.domain.todo.repository.impl;

import static com.eggmeonina.scrumble.domain.category.domain.QCategory.*;
import static com.eggmeonina.scrumble.domain.squadmember.domain.QSquadMember.*;
import static com.eggmeonina.scrumble.domain.todo.domain.QSquadToDo.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.dto.QSquadTodoCountResponse;
import com.eggmeonina.scrumble.domain.todo.dto.QSquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountResponse;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepositoryCustom;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SquadTodoRepositoryCustomImpl implements SquadTodoRepositoryCustom {

	private final JPAQueryFactory query;

	public List<SquadTodoResponse> findSquadTodos(Long squadMemberId, SquadTodoRequest request) {
		return query.select(
				new QSquadTodoResponse(squadToDo.toDo.id, squadToDo.id, squadToDo.toDo.contents, squadToDo.toDo.toDoAt,
					squadToDo.toDo.toDoStatus, category.id, category.categoryName, category.color)
			)
			.from(squadMember)
			.join(squadToDo)
			.on(squadMember.squad.id.eq(squadToDo.squad.id)
				.and(squadMember.member.id.eq(squadToDo.toDo.member.id)))
			.leftJoin(category)
			.on(category.id.eq(squadToDo.toDo.categoryId))
			.where(squadMember.id.eq(squadMemberId)
				.and(squadToDo.toDo.id.gt(request.getLastToDoId()))
				.and(squadToDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.toDoAt.between(request.getStartDate(), request.getEndDate()))
				.and(squadToDo.toDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.toDoType.eq(ToDoType.valueOf(request.getToDoType()))))
			.limit(request.getPageSize())
			.orderBy(squadToDo.toDo.toDoAt.desc(), squadToDo.toDo.id.asc())
			.fetch();
	}

	@Override
	public List<SquadTodoCountResponse> getSquadTodoCountSummary(Long memberId, Long squadMemberId,
		SquadTodoCountRequest request) {
		return query.select(
				new QSquadTodoCountResponse(
					squadToDo.toDo.toDoAt
					, squadToDo.toDo.countDistinct()
					, new CaseBuilder()
					.when(squadToDo.toDo.toDoStatus.eq(ToDoStatus.COMPLETED))
					.then(squadToDo.toDo.id)
					.otherwise((Expression<Long>) null)
					.count()
				)
			)
			.from(squadMember)
			.join(squadToDo)
			.on(squadMember.squad.id.eq(squadToDo.squad.id)
				.and(squadMember.member.id.eq(squadToDo.toDo.member.id)))
			.where(squadMember.id.eq(squadMemberId)
				.and(squadMember.member.id.eq(memberId))
				.and(squadToDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.toDoAt.between(request.startDate(), request.endDate()))
				.and(squadToDo.toDo.deletedFlag.eq(false))
				.and(squadToDo.toDo.toDoType.eq(ToDoType.valueOf(request.toDoType()))))
			.groupBy(squadToDo.toDo.toDoAt)
			.orderBy(squadToDo.toDo.toDoAt.desc())
			.fetch();
	}
}
