package com.eggmeonina.scrumble.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

/**
 * SseEmitter를 관리하는 Repository
 * 추후 scale-out되는 시점에 interface로 변환한다.
 */
@Repository
@RequiredArgsConstructor
public class SseEmitterRepository {

	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	public void save(Long id, SseEmitter emitter){
		emitters.put(id, emitter);
	}

	public void deleteById(Long id){
		emitters.remove(id);
	}

	public SseEmitter get(Long id){
		return emitters.get(id);
	}
}
