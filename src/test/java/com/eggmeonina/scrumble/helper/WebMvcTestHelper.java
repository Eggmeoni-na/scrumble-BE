package com.eggmeonina.scrumble.helper;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.eggmeonina.scrumble.domain.member.controller.MemberController;

@WebMvcTest(
	controllers = {
		MemberController.class
	}
)
public abstract class WebMvcTestHelper {
}
