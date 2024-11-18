package com.eggmeonina.scrumble.domain.squadmember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스쿼드장 위임할 멤버의 정보")
public class SquadMemberAssignRequest {

	@NotNull
	@Schema(description = "새로운 스쿼드장 멤버 ID")
	private Long newLeaderId;
}
