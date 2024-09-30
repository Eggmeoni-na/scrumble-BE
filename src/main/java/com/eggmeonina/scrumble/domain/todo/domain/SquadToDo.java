package com.eggmeonina.scrumble.domain.todo.domain;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "squad_todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SquadToDo extends BaseEntity {
	@Id
	@Column(name = "squad_todo_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "todo_id")
	private ToDo toDo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "squad_id")
	private Squad squad;

	@Column(name = "deleted_flag")
	private boolean deletedFlag;

	@Builder(builderMethodName = "create")
	public SquadToDo(ToDo toDo, Squad squad, boolean deletedFlag) {
		this.toDo = toDo;
		this.squad = squad;
		this.deletedFlag = deletedFlag;
	}
}
