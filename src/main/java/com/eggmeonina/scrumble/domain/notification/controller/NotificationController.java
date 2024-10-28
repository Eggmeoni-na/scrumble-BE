package com.eggmeonina.scrumble.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationsRequest;
import com.eggmeonina.scrumble.domain.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@Tag(name = "Notification 테스트", description = "notification용 API Controller")
@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/me")
	public ApiResponse<List<NotificationResponse>> findNotifications(
		@Parameter(hidden = true) @Member LoginMember member,
		@ModelAttribute NotificationsRequest notificationsRequest
	){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), notificationService.findNotifications(member.getMemberId(), notificationsRequest));
	}
}
