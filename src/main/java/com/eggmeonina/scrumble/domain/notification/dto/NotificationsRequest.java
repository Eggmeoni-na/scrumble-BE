package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "알림 조회 요청 DTO")
@AllArgsConstructor
public class NotificationsRequest {

	@Schema(description = "시작일시")
	private LocalDateTime startDateTime;
	@Schema(description = "종료일시")
	private LocalDateTime endDateTime;
	@Schema(description = "마지막 알림 Id")
	private Long lastNotificationId;
	@Schema(description = "페이징 사이즈")
	private int pageSize;

}
