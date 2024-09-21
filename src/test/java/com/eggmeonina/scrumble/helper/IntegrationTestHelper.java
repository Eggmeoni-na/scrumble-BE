package com.eggmeonina.scrumble.helper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.eggmeonina.scrumble.common.config.DatabaseClearExtension;

@ExtendWith(DatabaseClearExtension.class)
@SpringBootTest
public abstract class IntegrationTestHelper {
}
