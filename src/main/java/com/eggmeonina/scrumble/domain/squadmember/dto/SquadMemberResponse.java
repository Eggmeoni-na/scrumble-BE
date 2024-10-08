package com.eggmeonina.scrumble.domain.squadmember.dto;

import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadMemberResponse {
	private Long memberId;
	private String profileImg;
	private String name;
	private SquadMemberRole squadMemberRole;

	@QueryProjection
	public SquadMemberResponse(Long memberId, String profileImg, String name, SquadMemberRole squadMemberRole) {
		this.memberId = memberId;
		this.profileImg = profileImg;
		this.name = name;
		this.squadMemberRole = squadMemberRole;
	}
}
