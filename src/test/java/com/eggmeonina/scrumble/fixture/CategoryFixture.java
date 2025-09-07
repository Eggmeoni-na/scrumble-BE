package com.eggmeonina.scrumble.fixture;

import com.eggmeonina.scrumble.domain.category.domain.Category;

public class CategoryFixture {

	public static Category createCategory(Long memberId){
		return Category.create()
			.categoryName("회사")
			.color("000000")
			.memberId(memberId)
			.build();
	}
}
