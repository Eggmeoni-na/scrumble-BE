package com.eggmeonina.scrumble.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;

public class SquadTodoFixture {

	public static ToDo createToDo(Member newMember, String contents, ToDoStatus toDoStatus, boolean deletedFlag,
		LocalDate todoAt) {
		return ToDo.create()
			.toDoType(ToDoType.DAILY)
			.contents(contents)
			.toDoStatus(toDoStatus)
			.toDoAt(todoAt)
			.deletedFlag(deletedFlag)
			.member(newMember)
			.build();
	}

	public static Squad createSquad(String squadName, boolean deletedFlag) {
		return Squad.create()
			.squadName(squadName)
			.deletedFlag(deletedFlag)
			.build();
	}

	public static Member createMember(String name, String email, MemberStatus memberStatus, String oauthId) {
		return Member.create()
			.name(name)
			.email(email)
			.memberStatus(memberStatus)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation(oauthId, OauthType.GOOGLE))
			.build();
	}

	public static SquadToDo createSquadTodo(Squad newSquad, ToDo newTodo, boolean deletedFlag) {
		return SquadToDo.create()
			.squad(newSquad)
			.toDo(newTodo)
			.deletedFlag(deletedFlag)
			.build();
	}
}
