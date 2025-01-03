package com.eggmeonina.scrumble.domain.notification.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eggmeonina.scrumble.domain.notification.dto.NotificationSubScribeRequest;
import com.eggmeonina.scrumble.domain.notification.service.NotificationSender;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notification 연결 API", description = "notification 연결용 API Controller (스웨거가 SSE는 지원하지 않는 관계로 테스트는 불가)")
@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationConnectController {

	private final NotificationSender notificationSender;

	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(
		@ModelAttribute @Valid NotificationSubScribeRequest request
	) {
		return notificationSender.subscribe(request);
	}
}
