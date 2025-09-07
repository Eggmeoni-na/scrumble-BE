package com.eggmeonina.scrumble.domain.category.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.category.domain.Category;
import com.eggmeonina.scrumble.domain.category.dto.CategoryCreateRequest;
import com.eggmeonina.scrumble.domain.category.dto.CategoryResponse;
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
		Category foundCategory = categoryRepository.findByIdAndDeletedFlagFalse(categoryId)
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
		boolean isDuplicatedCategory = categoryRepository.existsByCategoryNameAndMemberIdAndDeletedFlagFalse(newCategory.getCategoryName(),
			newCategory.getMemberId());
		if(isDuplicatedCategory){
			throw new ExpectedException(CATEGORY_DUPLICATED);
		}
		categoryRepository.save(newCategory);
	}

	/**
	 * 카테고리 목록 조회
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<CategoryResponse> findCategories(Long memberId){
		return categoryRepository.findAllByMemberIdAndDeletedFlagFalse(memberId)
			.stream()
			.map(category -> new CategoryResponse(category.getId(), category.getCategoryName(), category.getColor()))
			.toList();
	}

	/**
	 * 카테고리 삭제
	 * @param memberId
	 * @param categoryId
	 */
	@Transactional
	public void deleteCategory(Long memberId, Long categoryId){
		Category foundCategory = categoryRepository.findByIdAndDeletedFlagFalse(categoryId)
			.orElseThrow(() -> new ExpectedException(CATEGORY_NOT_FOUND));

		if(!foundCategory.isOwnedBy(memberId))
			throw new ExpectedException(CATEGORY_ACCESS_DENIED);

		foundCategory.delete();
	}
}
