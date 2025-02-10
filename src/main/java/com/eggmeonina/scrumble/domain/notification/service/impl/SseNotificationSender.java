package com.eggmeonina.scrumble.domain.notification.service.impl;

import static com.eggmeonina.scrumble.domain.notification.domain.NotificationConstants.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
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
	private final ThreadPoolTaskScheduler taskScheduler;
	private final Map<Long, ScheduledFuture<?>> scheduleRepository = new ConcurrentHashMap<>();

	@Override
	public SseEmitter subscribe(NotificationSubScribeRequest request) {
		Long memberId = request.getMemberId();

		// Sse 연결 설정을 포함하는 SseEmitter 생성
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
		sseEmitterRepository.save(memberId, sseEmitter);

		// client에게 전달할 메세지 생성
		NotificationMessage notificationMessage =
			new NotificationMessage(notificationService.hasUnreadNotifications(request, memberId));
		// 메세지 전송
		sendNotification(memberId, NOTIFICATION_EVENT_NAME, notificationMessage);

		// 저장소에 이미 등록된 future 존재하면 제거
		ScheduledFuture<?> existingFuture = scheduleRepository.get(memberId);
		if(existingFuture != null){
			removeExistingSchedule(memberId);
		}

		// 스케줄러로 heartbeat 발행
		ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(() -> sendHeartbeat(memberId),
			Instant.now().plusSeconds(START_DELAY_SECONDS),
			Duration.ofSeconds(DELAY_SECONDS));

		// 스케줄러 저장소에 등록
		scheduleRepository.put(memberId, future);

		// sseEmitter의 연결 상태에 따른 콜백 메서드
		// onCompletion : 정상적으로 연결을 종료했을 때
		// onTimeout : 타임아웃 시간에 의해 연결이 끊겼을 때
		// onError : 클라이언트와의 연결이 비정상 종료되었을 때
		sseEmitter.onCompletion(() -> {
			log.debug("call onCompletion, memberId = {}", memberId);
			removeClient(memberId);
		});
		sseEmitter.onTimeout(() -> {
			log.debug("call onTimeout, memberId = {}", memberId);
			sseEmitter.complete();
		});

		// 클라이언트가 감지할 이벤트를 응답한다.
		return sseEmitter;
	}

	@Override
	public void sendNotification(Long memberId, NotificationMessage message) {
		sendNotification(memberId, NOTIFICATION_EVENT_NAME, message);
	}

	@Override
	public void sendNotification(Long memberId, String eventName, Object message) {
		// memberId 기준으로 저장된 SseEmitter를 조회한다.
		SseEmitter sseEmitter = sseEmitterRepository.get(memberId);
		if (sseEmitter == null) {
			log.debug("sendNotification sseEmitter null!! memberId = {}", memberId);
			return;
		}

		try {
			sseEmitter.send(SseEmitter.event().id(String.valueOf(memberId))
				.name(eventName)
				.data(new ResponseEntity<>(message, HttpStatus.OK)));
		} catch (IOException e) {
			if (e.getMessage() != null && e.getMessage().contains("Broken pipe")) {
				log.warn("Ignoring Broken pipe task. client connection closed.");
			} else {
				log.error("sseEmitter error memberId : {}, ex : {}", memberId, e.getMessage());
			}
		}
	}

	private void sendHeartbeat(Long memberId) {
		sendNotification(memberId, HEARTBEAT_EVENT_NAME, "ping");
	}

	private void removeClient(Long memberId) {
		log.debug("call removeClient, memberId = {}", memberId);
		SseEmitter sseEmitter = sseEmitterRepository.get(memberId);
		if (sseEmitter != null) {
			sseEmitterRepository.deleteById(memberId);
		}

		// 스케줄러에 예정된 스케줄 취소
		removeExistingSchedule(memberId);
	}

	private void removeExistingSchedule(Long memberId) {
		ScheduledFuture<?> scheduledFuture = scheduleRepository.get(memberId);
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			scheduleRepository.remove(memberId);
		}
	}

}
