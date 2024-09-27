package com.eggmeonina.scrumble.domain.squadmember.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadMemberResponse {
	private Long memberId;
	private String profileImg;
	private String name;

	@QueryProjection
	public SquadMemberResponse(Long memberId, String profileImg, String name) {
		this.memberId = memberId;
		this.profileImg = profileImg;
		this.name = name;
	}
}
