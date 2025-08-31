package com.eggmeonina.scrumble.domain.category.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.category.domain.Category;
import com.eggmeonina.scrumble.domain.category.dto.CategoryCreateRequest;
import com.eggmeonina.scrumble.domain.category.dto.CategoryUpdateRequest;
import com.eggmeonina.scrumble.domain.category.repository.CategoryRepository;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.fixture.MemberFixture;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class CategoryServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("카테고리 수정_정상")
	void changeCategory_success() {
		// given
		Category savedCategory = categoryRepository.save(Category.create()
			.categoryName("회사")
			.color("000000")
			.memberId(1L)
			.build());

		CategoryUpdateRequest request = new CategoryUpdateRequest("집안일", "FFFFFF");

		// when
		categoryService.changeCategory(1L, savedCategory.getId(), request);

		// then
		Category updatedCategory = categoryRepository.findById(savedCategory.getId()).get();
		assertThat(updatedCategory.getCategoryName()).isEqualTo("집안일");
		assertThat(updatedCategory.getColor()).isEqualTo("FFFFFF");
	}

	@Test
	@DisplayName("카테고리 수정_권한 없음_실패")
	void changeCategory_no_permission_fail() {
		// given
		Category savedCategory = categoryRepository.save(Category.create()
			.categoryName("회사")
			.color("000000")
			.memberId(1L)
			.build());

		CategoryUpdateRequest request = new CategoryUpdateRequest("집안일", "FFFFFF");

		// when & then
		assertThatThrownBy(() -> categoryService.changeCategory(2L, savedCategory.getId(), request))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(CATEGORY_ACCESS_DENIED.getMessage());
	}

	@Test
	@DisplayName("카테고리 수정_존재하지 않는 카테고리_실패")
	void changeCategory_category_not_found_fail() {
		// given
		CategoryUpdateRequest request = new CategoryUpdateRequest("집안일", "FFFFFF");

		// when & then
		assertThatThrownBy(() -> categoryService.changeCategory(1L, 1L, request))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(CATEGORY_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("카테고리 생성_성공")
	void createCategory_success() {
		// given
		CategoryCreateRequest request = new CategoryCreateRequest("카테고리1", "#FFFFFF");
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);

		// when
		categoryService.createCategory(testMember.getId(), request);

		// then
		Category newCategory = categoryRepository.findAll().get(0);

		assertSoftly(softly -> {
			softly.assertThat(newCategory.getCategoryName()).isEqualTo(request.categoryName());
			softly.assertThat(newCategory.getColor()).isEqualTo(request.color());
		});
	}
}
