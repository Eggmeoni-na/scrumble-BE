package com.eggmeonina.scrumble.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUnreadExistRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUnreadExistResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUpdateRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationsRequest;
import com.eggmeonina.scrumble.domain.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
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
	@Operation(summary = "알림 리스트 조회", description = "나의 알림 리스트를 조회한다")
	public ApiResponse<List<NotificationResponse>> findNotifications(
		@Parameter(hidden = true) @LoginMember Member member,
		@ModelAttribute NotificationsRequest notificationsRequest
	){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), notificationService.findNotifications(
			member.getId(), notificationsRequest));
	}

	@PutMapping("/{notificationId}")
	@Operation(summary = "알림 상태 변경", description = "알림 상태를 변경한다 (readFlag, notificationStatus)")
	public ApiResponse<NotificationResponse> updateNotification(
		@Parameter(hidden = true) @LoginMember Member member,
		@PathVariable Long notificationId,
		@RequestBody NotificationUpdateRequest notificationUpdateRequest
	){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(),
			notificationService.updateNotification(member, notificationId, notificationUpdateRequest));
	}

	@GetMapping("/unread-exists")
	@Operation(summary = "알림 읽지 않음 여부 조회", description = "알림 읽지 않음 여부를 조회한다")
	public ApiResponse<NotificationUnreadExistResponse> hasUnreadNotifications(
		@Parameter(hidden = true) @LoginMember Member member,
		@ModelAttribute NotificationUnreadExistRequest request
	){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(),
			notificationService.hasUnreadNotifications(request, member.getId()));
	}
}
