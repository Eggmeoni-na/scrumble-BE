package com.eggmeonina.scrumble.domain.member.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.MemberException;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "name", nullable = true)
	private String name;
	@Column(name = "profile_image", nullable = true)
	private String profileImage;
	@Embedded
	private OauthInformation oauthInformation;
	@Enumerated(EnumType.STRING)
	@Column(name = "member_status", nullable = false)
	private MemberStatus memberStatus;
	@CreationTimestamp
	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;
	@Column(name = "leaved_at", nullable = true)
	private LocalDateTime leavedAt;

	@Builder(builderMethodName = "create")
	public Member(String email, String name, String profileImage, OauthInformation oauthInformation,
		MemberStatus memberStatus, LocalDateTime joinedAt) {
		this.email = email;
		this.name = name;
		this.profileImage = profileImage;
		this.oauthInformation = oauthInformation;
		this.memberStatus = memberStatus;
		this.joinedAt = joinedAt;
	}

	public Member(Long id, String email, String name, String profileImage, OauthInformation oauthInformation,
		MemberStatus memberStatus, LocalDateTime joinedAt) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.profileImage = profileImage;
		this.oauthInformation = oauthInformation;
		this.memberStatus = memberStatus;
		this.joinedAt = joinedAt;
	}

	public void withdraw(){
		if(this.memberStatus == MemberStatus.WITHDRAW){
			throw new MemberException(MEMBER_ALREADY_WITHDRAW);
		}
		this.memberStatus = MemberStatus.WITHDRAW;
		this.leavedAt = LocalDateTime.now();
	}
}
