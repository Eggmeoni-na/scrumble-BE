package com.eggmeonina.scrumble.domain.membership.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.MembershipException;
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
@Table(name = "membership")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="membership_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "squad_id")
	private Squad squad;
	@Enumerated(EnumType.STRING)
	@Column(name = "membership_status", nullable = false)
	private MembershipStatus membershipStatus;
	@Enumerated(EnumType.STRING)
	@Column(name = "membership_role", nullable = false)
	private MembershipRole membershipRole;

	@Builder(builderMethodName = "create")
	public Membership(Member member, Squad squad, MembershipStatus membershipStatus, MembershipRole membershipRole) {
		this.membershipStatus = membershipStatus;
		this.membershipRole = membershipRole;
		if(member == null || squad == null){
			throw new MembershipException(MEMBER_OR_GROUP_NOT_FOUND);
		}
		this.member = member;
		this.squad = squad;
	}
}
