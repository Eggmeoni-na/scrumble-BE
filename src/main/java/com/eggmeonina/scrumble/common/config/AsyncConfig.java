package com.eggmeonina.scrumble.common.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean(name = "taskExecutor")
	public ThreadPoolTaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // 기본 쓰레드 크기
		executor.setQueueCapacity(20);
		executor.setMaxPoolSize(10);
		// queue 사이즈를 넘었을 때 exception이 발생한다. 이때 어떻게 처리할지 결정하는 구현체
		// AbortPolicy : RejectExecutionHandler 예외 발생 시킴
		// DiscardOldestPolicy : 오래된 작업을 skip
		// DiscardPolicy : 처리하려는 작업을 skip
		// CallerRunsPolicy : 요청한 caller에서 직접 처리
		executor.setThreadNamePrefix("AsyncExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	@Bean(name = "taskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5); // 기본 쓰레드 크기
		scheduler.setThreadNamePrefix("TaskScheduler-");
		return scheduler;
	}
}
