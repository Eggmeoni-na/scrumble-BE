package com.eggmeonina.scrumble.domain.category.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.category.domain.Category;
import com.eggmeonina.scrumble.domain.category.dto.CategoryCreateRequest;
import com.eggmeonina.scrumble.domain.category.dto.CategoryUpdateRequest;
import com.eggmeonina.scrumble.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	/**
	 * 카테고리 수정
	 * @param memberId
	 * @param categoryId
	 * @param request
	 */
	@Transactional
	public void changeCategory(Long memberId, Long categoryId, CategoryUpdateRequest request){
		Category foundCategory = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new ExpectedException(CATEGORY_NOT_FOUND));

		foundCategory.updateCategory(memberId, request.getCategoryName(), request.getColor());
	}

	/**
	 * 카테고리 등록
	 * @param memberId
	 * @param request
	 */
	@Transactional
	public void createCategory(Long memberId, CategoryCreateRequest request){
		Category newCategory = CategoryCreateRequest.to(memberId, request);
		boolean isDuplicatedCategory = categoryRepository.existsByCategoryNameAndMemberId(newCategory.getCategoryName(),
			newCategory.getMemberId());
		if(isDuplicatedCategory){
			throw new ExpectedException(CATEGORY_DUPLICATED);
		}
		categoryRepository.save(newCategory);
	}
}
