package com.eggmeonina.scrumble.domain.notification.dto;

import com.eggmeonina.scrumble.domain.notification.domain.NotificationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알림 변경 요청 객체")
public class NotificationUpdateRequest {
	@Schema(description = "알림 상태 - PENDING(전송), COMPLETED(완료)")
	private NotificationStatus notificationStatus;
	@Schema(description = "알림 읽기 여부 - true(읽음), false(읽지 않음)")
	private boolean readFlag;
}
