package com.eggmeonina.scrumble.helper;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.eggmeonina.scrumble.domain.member.controller.UserController;

@WebMvcTest(
	controllers = {
		UserController.class
	}
)
public abstract class WebMvcTestHelper {
}
