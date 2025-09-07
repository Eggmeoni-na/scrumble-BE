package com.eggmeonina.scrumble.domain.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryNameAndMemberId(String categoryName, Long memberId);

	List<Category> findAllByMemberIdAndDeletedFlagFalse(Long memberId);
}
