package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "읽지 않은 알림 여부 조회 DTO")
@AllArgsConstructor
public class NotificationUnreadExistRequest {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "시작일시")
	private LocalDateTime startDateTime;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "종료일시")
	private LocalDateTime endDateTime;
}
