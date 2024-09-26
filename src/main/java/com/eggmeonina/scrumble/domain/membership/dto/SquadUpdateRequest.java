package com.eggmeonina.scrumble.domain.membership.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquadUpdateRequest {
	@NotEmpty
	private String squadName;
}
