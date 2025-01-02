package com.eggmeonina.scrumble.domain.notification.service.impl;

import static com.eggmeonina.scrumble.domain.notification.domain.NotificationConstants.*;

import java.io.IOException;

import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eggmeonina.scrumble.domain.notification.dto.NotificationMessage;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationSubScribeRequest;
import com.eggmeonina.scrumble.domain.notification.repository.SseEmitterRepository;
import com.eggmeonina.scrumble.domain.notification.service.NotificationSender;
import com.eggmeonina.scrumble.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseNotificationSender implements NotificationSender {

	private final SseEmitterRepository sseEmitterRepository;
	private final NotificationService notificationService;

	@Override
	public SseEmitter subscribe(NotificationSubScribeRequest request) {
		// Sse 연결 설정을 포함하는 SseEmitter 생성
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
		sseEmitterRepository.save(request.getMemberId(), sseEmitter);

		// client에게 전달할 메세지 생성
		NotificationMessage notificationMessage =
			new NotificationMessage(notificationService.existsUnreadNotifications(request, request.getMemberId()));
		sendNotification(request.getMemberId(), notificationMessage);

		// sseEmitter의 연결 상태에 따른 콜백 메서드
		// onCompletion : 정상적으로 연결을 종료했을 때
		// onTimeout : 타임아웃 시간에 의해 연결이 끊겼을 때
		// onError : 클라이언트와의 연결이 비정상 종료되었을 때
		sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(request.getMemberId()));
		sseEmitter.onTimeout(() -> sseEmitterRepository.deleteById(request.getMemberId()));
		sseEmitter.onError(throwable -> {
			log.error("sseEmitter error memberId : {}, ex : {}", request.getMemberId(), throwable.getMessage());
			sseEmitterRepository.deleteById(request.getMemberId());
		});

		// 클라이언트가 감지할 이벤트를 응답한다.
		return sseEmitter;
	}

	@Override
	public void sendNotification(Long memberId, NotificationMessage message) {
		// memberId 기준으로 저장된 SseEmitter를 조회한다.
		SseEmitter sseEmitter = sseEmitterRepository.get(memberId);
		if (sseEmitter == null) {
			log.debug("sendNotification sseEmitter nul!! memberId = {}", memberId);
			return;
		}

		try {
			sseEmitter.send(SseEmitter.event().id(String.valueOf(memberId)).name(notificationEventName)
				.data(new ResponseEntity<>(message, HttpStatus.OK)));
		} catch (IOException e) {
			sseEmitterRepository.deleteById(memberId);
			sseEmitter.completeWithError(e);
		}
	}


}
