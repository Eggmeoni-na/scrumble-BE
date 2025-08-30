package com.eggmeonina.scrumble.domain.category.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.category.domain.Category;
import com.eggmeonina.scrumble.domain.category.dto.CategoryUpdateRequest;
import com.eggmeonina.scrumble.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	@Transactional
	public void changeCategory(Long memberId, CategoryUpdateRequest request){
		Category foundCategory = categoryRepository.findById(request.getCategoryId())
			.orElseThrow(() -> new ExpectedException(CATEGORY_NOT_FOUND));

		foundCategory.updateCategory(memberId, request.getCategoryName(), request.getColor());
	}
}
