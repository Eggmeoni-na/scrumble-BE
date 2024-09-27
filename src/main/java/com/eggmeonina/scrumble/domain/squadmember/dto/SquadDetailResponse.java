package com.eggmeonina.scrumble.domain.squadmember.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadDetailResponse {

	private Long squadId;
	private String squadName;
	private List<SquadMemberResponse> squadMembers;

	@QueryProjection
	public SquadDetailResponse(Long squadId, String squadName, List<SquadMemberResponse> squadMembers) {
		this.squadId = squadId;
		this.squadName = squadName;
		this.squadMembers = squadMembers;
	}
}
