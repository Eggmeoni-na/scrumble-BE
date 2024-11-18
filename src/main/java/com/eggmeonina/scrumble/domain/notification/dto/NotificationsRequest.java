package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "알림 조회 요청 DTO")
@AllArgsConstructor
public class NotificationsRequest {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "시작일시")
	private LocalDateTime startDateTime;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "종료일시")
	private LocalDateTime endDateTime;
	@Schema(description = "마지막 알림 Id")
	private Long lastNotificationId;
	@Schema(description = "페이징 사이즈")
	private int pageSize;

}
