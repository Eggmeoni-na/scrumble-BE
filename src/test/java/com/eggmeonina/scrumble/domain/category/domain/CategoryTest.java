package com.eggmeonina.scrumble.domain.category.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.fixture.CategoryFixture;

class CategoryTest {

	@Test
	@DisplayName("카테고리 수정_정상")
	void update_success() {
		// given
		Category newCategory = CategoryFixture.createCategory(1L);
		
		// when
		Long memberId = 1L;
		String newCategoryName = "집안일";
		String newColor = "FFFFFF";

		newCategory.updateCategory(memberId, newCategoryName, newColor);

		// then
		assertThat(newCategory.getCategoryName()).isEqualTo(newCategoryName);
		assertThat(newCategory.getColor()).isEqualTo(newColor);
	}

	@Test
	@DisplayName("권한이 없는 유저가 카테고리 수정_실패")
	void update_no_authority_fail() {
		// given
		Category newCategory = CategoryFixture.createCategory(1L);

		// when
		Long anotherId = 2L;
		String newCategoryName = "집안일";
		String newColor = "FFFFFF";

		assertThatThrownBy(() -> newCategory.updateCategory(anotherId, newCategoryName, newColor))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(CATEGORY_ACCESS_DENIED.getMessage());
	}

	@Test
	@DisplayName("카테고리 삭제_정상")
	void delete_success() {
		var newCategory = CategoryFixture.createCategory(1L);

		newCategory.delete();

		assertThat(newCategory.isDeletedFlag()).isTrue();

	}
}
