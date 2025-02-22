package com.eggmeonina.scrumble.domain.event.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.eggmeonina.scrumble.common.domain.BaseEntity;

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
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

	@Id
	@Column(name = "event_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "domain_type", nullable = false)
	private String domainType;

	@Column(name = "event_type", nullable = true)
	private String eventType;

	@Column(name = "event_data", nullable = false)
	private String eventData;

	@UpdateTimestamp
	@Column(name = "published_at", nullable = false)
	private LocalDateTime publishedAt;

	@Column(name = "published_flag", nullable = false)
	private boolean publishedFlag;

	@Builder(builderMethodName = "create")
	public Event(String domainType, String eventType, String eventData, LocalDateTime publishedAt,
		boolean publishedFlag) {
		this.domainType = domainType;
		this.eventType = eventType;
		this.eventData = eventData;
		this.publishedAt = publishedAt;
		this.publishedFlag = publishedFlag;
	}

	public Event(String domainType, String eventType, LocalDateTime publishedAt, boolean publishedFlag) {
		this(domainType, eventType, null, publishedAt, publishedFlag);
	}

	public void publish() {
		publishedFlag = true;
	}
}
