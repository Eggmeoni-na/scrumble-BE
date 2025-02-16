package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "알림 연결(구독)용 DTO")
public class NotificationSubScribeRequest {

	@NotNull
	private Long memberId;
	@NotNull(message = "시작일시는 null이어서는 안 됩니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "시작일시")
	private LocalDateTime startDateTime;
	@NotNull(message = "종료일시는 null이어서는 안 됩니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "종료일시")
	private LocalDateTime endDateTime;
	private Long lastNotificationId;
	private int pageSize;

	public NotificationSubScribeRequest(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.memberId = memberId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.lastNotificationId = 9_999_999_999L;
		this.pageSize = 30;
	}

}
