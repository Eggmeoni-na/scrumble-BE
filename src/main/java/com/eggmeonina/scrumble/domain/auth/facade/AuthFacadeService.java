package com.eggmeonina.scrumble.domain.auth.facade;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eggmeonina.scrumble.domain.auth.client.AuthClient;
import com.eggmeonina.scrumble.domain.auth.dto.GoogleAuthClientResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.auth.dto.OauthRequest;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.member.service.MemberService;

@Service
public class AuthFacadeService {

	private final Map<String, AuthClient> authClients;
	private final MemberService memberService;

	@Autowired
	public AuthFacadeService(Map<String, AuthClient> authClients, MemberService memberService) {
		this.memberService = memberService;
		this.authClients = Collections.unmodifiableMap(authClients);
	}

	public LoginMember getToken(OauthRequest request){
		AuthClient authClient = authClients.get(request.getOauthType().getAuthClientName());
		if(authClient == null){
			throw new RuntimeException("지원하지 않는 type입니다.");
		}
		GoogleAuthClientResponse accessToken = authClient.getAccessToken(request.getCode(), request.getScope());
		String token = String.format("%s %s", accessToken.getTokenType(), accessToken.getAccessToken());
		MemberInformation foundMemberInformation = authClient.findUserProfile(token);
		return memberService.login(foundMemberInformation, request.getOauthType());
	}
}
