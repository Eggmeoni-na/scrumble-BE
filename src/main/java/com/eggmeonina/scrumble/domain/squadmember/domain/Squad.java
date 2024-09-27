package com.eggmeonina.scrumble.domain.squadmember.domain;

import com.eggmeonina.scrumble.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "squad")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Squad extends BaseEntity {

	@Id
	@Column(name = "squad_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "squad_name", nullable = false)
	private String squadName;
	@Column(name = "deleted_flag", nullable = false)
	private boolean deletedFlag;

	@Builder(builderMethodName = "create")
	public Squad(String squadName, boolean deletedFlag) {
		this.squadName = squadName;
		this.deletedFlag = deletedFlag;
	}

	public Squad(Long id, String squadName, boolean deletedFlag) {
		this.id = id;
		this.squadName = squadName;
		this.deletedFlag = deletedFlag;
	}

	public void rename(String newSquadName){
		this.squadName = newSquadName;
	}
}
