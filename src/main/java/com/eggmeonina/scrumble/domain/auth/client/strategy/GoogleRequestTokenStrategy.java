package com.eggmeonina.scrumble.domain.auth.client.strategy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientRequest;
import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientResponse;

@FeignClient(name = "google-token", url ="${oauth.google.token-uri}")
public interface GoogleRequestTokenStrategy {
	@PostMapping
	GoogleAuthClientResponse getAccessToken(@RequestBody GoogleAuthClientRequest request);
}
