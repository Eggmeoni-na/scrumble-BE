package com.eggmeonina.scrumble.domain.auth.client.strategy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;

@FeignClient(name = "google-resource", url ="${oauth.google.resource-uri}")
public interface GoogleRequestResourceStrategy {

	@GetMapping
	MemberInformation findUserInfo(@RequestHeader("Authorization") String accessToken);
}
