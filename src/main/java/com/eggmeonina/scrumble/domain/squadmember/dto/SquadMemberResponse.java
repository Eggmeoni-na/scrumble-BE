package com.eggmeonina.scrumble.domain.squadmember.dto;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadMemberResponse {
	private Long memberId;
	private Long squadMemberId;
	private String profileImg;
	private String name;
	private SquadMemberRole squadMemberRole;

	@QueryProjection
	public SquadMemberResponse(Long memberId, Long squadMemberId, String profileImg, String name,
		SquadMemberRole squadMemberRole) {
		this.memberId = memberId;
		this.squadMemberId = squadMemberId;
		this.profileImg = profileImg;
		this.name = name;
		this.squadMemberRole = squadMemberRole;
	}
}
