package com.eggmeonina.scrumble.domain.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.category.dto.CategoryCreateRequest;
import com.eggmeonina.scrumble.domain.category.dto.CategoryResponse;
import com.eggmeonina.scrumble.domain.category.dto.CategoryUpdateRequest;
import com.eggmeonina.scrumble.domain.category.service.CategoryService;
import com.eggmeonina.scrumble.domain.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Category 테스트", description = "카테고리 생성, 삭제 조회용 API")
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController {

	private final CategoryService categoryService;

	@PutMapping("/{categoryId}")
	@Operation(summary = "카테고리명, 컬러를 수정한다")
	@Parameter(name = "member", hidden = true)
	public ApiResponse<Void> updateCategory(
		@LoginMember Member member,
		@PathVariable Long categoryId,
		@RequestBody @Valid CategoryUpdateRequest request
	) {
		categoryService.changeCategory(member.getId(), categoryId, request);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PostMapping
	@Operation(summary = "카테고리를 새로 등록한다")
	@Parameter(name = "member", hidden = true)
	public ApiResponse<Void> createCategory(
		@LoginMember Member member,
		@RequestBody @Valid CategoryCreateRequest request
	) {
		categoryService.createCategory(member.getId(), request);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@GetMapping
	@Operation(summary = "카테고리 리스트를 조회한다")
	@Parameter(name = "member", hidden = true)
	public ApiResponse<List<CategoryResponse>> findCategories(@LoginMember Member member) {
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), categoryService.findCategories(member.getId()));
	}

	@DeleteMapping("/{categoryId}")
	@Operation(summary = "카테고리를 삭제한다")
	@Parameter(name = "member", hidden = true)
	public ApiResponse<Void> deleteCategory(@LoginMember Member member, @PathVariable Long categoryId){
		categoryService.deleteCategory(member.getId(), categoryId);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

}
