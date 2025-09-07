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
import com.eggmeonina.scrumble.fixture.CategoryFixture;
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

	@Test
	@DisplayName("중복 카테고리명으로 생성_실패")
	void createCategory_duplicated_fail(){
		// given
		CategoryCreateRequest request = new CategoryCreateRequest("카테고리1", "#FFFFFF");
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);
		categoryService.createCategory(testMember.getId(), request);

		CategoryCreateRequest duplicatedCategory = new CategoryCreateRequest("카테고리1", "#000000");

		assertThatThrownBy(()->
			categoryService.createCategory(testMember.getId(), duplicatedCategory))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(CATEGORY_DUPLICATED.getMessage());
	}

	@Test
	@DisplayName("카테고리 목록 조회_정상")
	void findCategories_success() {
		// given
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);

		Category category1 = categoryRepository.save(Category.create()
			.categoryName("회사업무")
			.color("#FF0000")
			.memberId(testMember.getId())
			.build());

		Category category2 = categoryRepository.save(Category.create()
			.categoryName("개인업무")
			.color("#00FF00")
			.memberId(testMember.getId())
			.build());

		// when
		var result = categoryService.findCategories(testMember.getId());

		// then
		assertThat(result).hasSize(2);
		assertSoftly(softly -> {
			softly.assertThat(result.get(0).categoryId()).isEqualTo(category1.getId());
			softly.assertThat(result.get(0).categoryName()).isEqualTo("회사업무");
			softly.assertThat(result.get(0).color()).isEqualTo("#FF0000");
			
			softly.assertThat(result.get(1).categoryId()).isEqualTo(category2.getId());
			softly.assertThat(result.get(1).categoryName()).isEqualTo("개인업무");
			softly.assertThat(result.get(1).color()).isEqualTo("#00FF00");
		});
	}

	@Test
	@DisplayName("카테고리 목록 조회_다른 회원의 카테고리는 조회되지 않음")
	void findCategories_only_returns_own_categories() {
		// given
		Member testMember1 = MemberFixture.createJOINMember("test1@test.com", "test1", "123324");
		Member testMember2 = MemberFixture.createJOINMember("test2@test.com", "test2", "123325");
		memberRepository.save(testMember1);
		memberRepository.save(testMember2);

		categoryRepository.save(Category.create()
			.categoryName("회원1카테고리")
			.color("#FF0000")
			.memberId(testMember1.getId())
			.build());

		categoryRepository.save(Category.create()
			.categoryName("회원2카테고리")
			.color("#00FF00")
			.memberId(testMember2.getId())
			.build());

		// when
		var result = categoryService.findCategories(testMember1.getId());

		// then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).categoryName()).isEqualTo("회원1카테고리");
	}

	@Test
	@DisplayName("카테고리 목록 조회_빈 결과")
	void findCategories_empty_result() {
		// given
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);

		// when
		var result = categoryService.findCategories(testMember.getId());

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("카테고리 삭제_정상")
	void delete_success() {
		// given
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);
		Category createCategory = CategoryFixture.createCategory(testMember.getId());
		categoryRepository.save(createCategory);

		// when
		categoryService.deleteCategory(testMember.getId(), createCategory.getId());

		// then
		Category foundCategory = categoryRepository.findById(createCategory.getId()).get();
		assertThat(foundCategory.isDeletedFlag()).isTrue();
	}

	@Test
	@DisplayName("타인의 카테고리 삭제_실패")
	void delete_not_owner_fail() {
		// given
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);
		Category createCategory = CategoryFixture.createCategory(testMember.getId());
		categoryRepository.save(createCategory);

		Member anotherMember = MemberFixture.createJOINMember("test2@test.com", "test2", "1233324");
		memberRepository.save(anotherMember);

		assertThatThrownBy(() -> categoryService.deleteCategory(anotherMember.getId(), createCategory.getId()))
			.hasMessageContaining(CATEGORY_ACCESS_DENIED.getMessage())
			.isInstanceOf(ExpectedException.class);
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 삭제_실패")
	void delete_not_found_fail() {
		// given
		Member testMember = MemberFixture.createJOINMember("test@test.com", "test", "123324");
		memberRepository.save(testMember);

		assertThatThrownBy(()-> categoryService.deleteCategory(testMember.getId(), 1L))
			.hasMessageContaining(CATEGORY_NOT_FOUND.getMessage())
			.isInstanceOf(ExpectedException.class);
	}
}
