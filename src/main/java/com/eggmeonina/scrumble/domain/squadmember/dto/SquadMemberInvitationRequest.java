package com.eggmeonina.scrumble.domain.squadmember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스쿼드 멤버 초대 응답 객체")
public class SquadMemberInvitationRequest {
	@NotNull
	@Schema(description = "스쿼드 초대할 멤버 ID")
	private Long newMemberId;
}
