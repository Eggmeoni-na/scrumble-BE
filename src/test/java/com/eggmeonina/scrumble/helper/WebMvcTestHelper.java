package com.eggmeonina.scrumble.helper;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import com.eggmeonina.scrumble.domain.member.controller.UserController;

@WebMvcTest(
	controllers = {
		UserController.class
	}
)
@ActiveProfiles("test")
public abstract class WebMvcTestHelper {
}
