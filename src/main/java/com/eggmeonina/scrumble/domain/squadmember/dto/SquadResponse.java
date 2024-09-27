package com.eggmeonina.scrumble.domain.squadmember.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquadResponse {
	private Long squadId;
	private String squadName;

	@QueryProjection
	public SquadResponse(Long squadId, String squadName) {
		this.squadId = squadId;
		this.squadName = squadName;
	}
}
