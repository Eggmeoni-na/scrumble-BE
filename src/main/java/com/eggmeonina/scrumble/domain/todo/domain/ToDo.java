package com.eggmeonina.scrumble.domain.todo.domain;

import java.time.LocalDate;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
	private ToDoStatus toDoStatus;

	@Column(name = "todo_at", nullable = false)
	private LocalDate toDoAt;

	@Column(name = "deleted_flag", nullable = false)
	private boolean deletedFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "categoryId", nullable = true)
	private Long categoryId;

	@Builder(builderMethodName = "create")
	public ToDo(ToDoType toDoType, String contents, ToDoStatus toDoStatus, LocalDate toDoAt, boolean deletedFlag,
		Member member, Long categoryId) {
		this.toDoType = toDoType;
		this.contents = contents;
		this.toDoStatus = toDoStatus;
		this.toDoAt = toDoAt;
		this.deletedFlag = deletedFlag;
		this.member = member;
		this.categoryId = categoryId;
		initValid(member, contents, toDoType);
	}

	private void initValid(Member member, String contents, ToDoType toDoType){
		if(member == null){
			throw new MemberException(ErrorCode.MEMBER_NOT_FOUND);
		}
		if(contents == null || contents.isBlank()){
			throw new ToDoException(ErrorCode.TODO_CONTENTS_NOT_BLANK);
		}
		if(toDoType == null){
			throw new ToDoException(ErrorCode.TODO_TYPE_NOT_NULL);
		}
	}

	public void delete(){
		this.deletedFlag = true;
	}

	public void update(String newContents, ToDoStatus newToDoStatus, LocalDate newTodoAt, Long categoryId){
		this.contents = newContents;
		this.toDoStatus = newToDoStatus;
		this.toDoAt = newTodoAt;
		this.categoryId = categoryId;
	}
}
