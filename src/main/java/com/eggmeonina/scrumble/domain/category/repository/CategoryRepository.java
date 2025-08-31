package com.eggmeonina.scrumble.domain.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryNameAndMemberId(String categoryName, Long memberId);

	@Query("""
	SELECT c
	  FROM Category c
	 WHERE c.memberId = :memberId AND c.deletedFlag = false
	""")
	List<Category> findAllByMemberIdAndDeletedFlagNot(Long memberId);
}
