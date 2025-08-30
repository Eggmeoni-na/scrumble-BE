package com.eggmeonina.scrumble.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리의 정보")
public class CategoryUpdateRequest {

	@NotNull(message = "카테고리 ID는 null일 수 없습니다.")
	@Schema(description = "수정할 카테고리 ID")
	private Long categoryId;

	@NotBlank(message = "카테고리명은 비어있거나 null일 수 없습니다.")
	@Schema(description = "수정할 카테고리명 혹은 기존 카테고리명")
	private String categoryName;

	@NotBlank(message = "색상은 비어있거나 null일 수 없습니다.")
	@Schema(description = "수정할 색상 혹은 기존 색상")
	private String color;
}
