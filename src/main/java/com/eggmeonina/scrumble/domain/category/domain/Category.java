package com.eggmeonina.scrumble.domain.category.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import java.util.Objects;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.ExpectedException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "color")
	private String color;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "deleted_flag", nullable = false)
	private boolean deletedFlag;

	@Builder(builderMethodName = "create")
	public Category(String categoryName, String color, Long memberId) {
		this.categoryName = categoryName;
		this.color = color;
		this.memberId = memberId;
		this.deletedFlag = false;
	}

	public void updateCategory(Long requesterId, String newCategoryName, String newColor){
		if (!isOwnedBy(requesterId)) {
			throw new ExpectedException(CATEGORY_ACCESS_DENIED);
		}
		this.categoryName = newCategoryName;
		this.color = newColor;
	}

	public boolean isOwnedBy(Long memberId) {
		return Objects.equals(this.memberId, memberId);
	}
}
