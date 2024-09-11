package com.eggmeonina.scrumble.common.domain;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
