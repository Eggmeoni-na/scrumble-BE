package com.eggmeonina.scrumble.domain.todo.domain;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.domain.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDo extends BaseEntity {

	@Id
	@Column(name = "todo_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "todo_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ToDoType toDoType;

	@Column(name = "contents", nullable = false)
	private String contents;

	@Column(name = "todo_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private TodoStatus todoStatus;

	@Column(name = "deleted_flag")
	private boolean deletedFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder(buildMethodName = "create")
	public ToDo(ToDoType toDoType, String contents, TodoStatus todoStatus, boolean deletedFlag, Member member) {
		this.toDoType = toDoType;
		this.contents = contents;
		this.todoStatus = todoStatus;
		this.deletedFlag = deletedFlag;
		this.member = member;
	}
}
