package com.eggmeonina.scrumble.domain.squadmember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강퇴할 멤버의 정보")
public class SquadMemberKickRequest {
	@NotNull
	@Schema(description = "강퇴할 멤버 ID")
	private Long kickedMemberId;
}
