package com.eggmeonina.scrumble.domain.membership.domain;

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
@Table(name = "groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

	@Id
	@Column(name = "group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "group_name", nullable = false)
	private String groupName;
	@Column(name = "deleted_flag", nullable = false)
	private boolean deletedFlag;

	@Builder(builderMethodName = "create")
	public Group(String groupName, boolean deletedFlag) {
		this.groupName = groupName;
		this.deletedFlag = deletedFlag;
	}
}
