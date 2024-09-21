package com.eggmeonina.scrumble.domain.membership.dto;

import com.eggmeonina.scrumble.domain.membership.domain.Squad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquadCreateRequest {

	@NotBlank
	private String groupName;

	public static Squad to(SquadCreateRequest request){
		return Squad.create()
			.squadName(request.getGroupName())
			.deletedFlag(false)
			.build();
	}
}
