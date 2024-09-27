package com.eggmeonina.scrumble.domain.squadmember.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.SquadMemberException;
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

@Entity
@Getter
@Table(name = "squad_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SquadMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="squad_member_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "squad_id")
	private Squad squad;
	@Enumerated(EnumType.STRING)
	@Column(name = "squad_member_status", nullable = false)
	private SquadMemberStatus squadMemberStatus;
	@Enumerated(EnumType.STRING)
	@Column(name = "squad_member_role", nullable = false)
	private SquadMemberRole squadMemberRole;

	@Builder(builderMethodName = "create")
	public SquadMember(Member member, Squad squad, SquadMemberStatus squadMemberStatus, SquadMemberRole squadMemberRole) {
		this.squadMemberStatus = squadMemberStatus;
		this.squadMemberRole = squadMemberRole;
		if(member == null || squad == null){
			throw new SquadMemberException(MEMBER_OR_SQUAD_NOT_FOUND);
		}
		this.member = member;
		this.squad = squad;
	}

	public boolean isLeader(){
		return squadMemberRole.equals(SquadMemberRole.LEADER);
	}

	public void assignLeader(){
		if(isLeader()) {
			return;
		}
		this.squadMemberRole = SquadMemberRole.LEADER;
	}

	public void resignAsLeader(){
		if(!isLeader()){
			return;
		}
		this.squadMemberRole = SquadMemberRole.NORMAL;
	}
}
