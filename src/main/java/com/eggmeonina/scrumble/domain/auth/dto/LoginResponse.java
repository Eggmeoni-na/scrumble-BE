package com.eggmeonina.scrumble.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인한 멤버의 정보")
public class LoginResponse {
	@Schema(description = "멤버 ID")
	private Long id;
	@Schema(description = "닉네임")
	private String name;

	public static LoginResponse from(MemberInfo member){
		return new LoginResponse(member.getMemberId(), member.getName());
	}
}
