package com.eggmeonina.scrumble.domain.category.dto;

import com.eggmeonina.scrumble.domain.category.domain.Category;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
	@NotBlank(message = "카테고리명은 null이거나 빈 값일 수 없습니다.") String categoryName
	, @NotBlank(message = "색상은 null이거나 빈 값일 수 없습니다.") String color) {
	public static Category to(Long memberId, CategoryCreateRequest request){
		return new Category(request.categoryName(), request.color(), memberId);
	}
}
