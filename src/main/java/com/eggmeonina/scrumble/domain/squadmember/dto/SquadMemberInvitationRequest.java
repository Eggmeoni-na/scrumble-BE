package com.eggmeonina.scrumble.domain.squadmember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스쿼드 멤버 초대 요청 객체")
public class SquadMemberInvitationRequest {
	@NotNull
	@Schema(description = "초대될 멤버 ID")
	private Long memberId;
}
