package com.eggmeonina.scrumble.domain.notification.domain;

import static com.eggmeonina.scrumble.domain.notification.domain.NotificationType.*;

import java.util.Arrays;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.common.util.JsonToNotificationMessageConverter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {

	INVITE_REQUEST_MESSAGE(INVITE_REQUEST, "{userName}님이 {squadName} 스쿼드에 초대하였습니다"),
	INVITE_ACCEPT_MESSAGE(INVITE_ACCEPT, "{userName}님이 {squadName} 스쿼드에 참여하였습니다");

	private final NotificationType notificationType;
	private final String message;

	public static String getNotificationMessage(NotificationType notificationType, String jsonData) {
		NotificationMessage message = findNotificationMessage(notificationType);
		return JsonToNotificationMessageConverter.jsonToNotificationMessage(message.getMessage(), jsonData);
	}

	private static NotificationMessage findNotificationMessage(NotificationType notificationType) {
		return Arrays.stream(NotificationMessage.values())
			.filter(notificationMessage -> notificationMessage.getNotificationType().equals(notificationType))
			.findFirst()
			.orElseThrow(() -> new ExpectedException(ErrorCode.NOTIFICATION_TYPE_NOT_EXISTS));
	}

}
