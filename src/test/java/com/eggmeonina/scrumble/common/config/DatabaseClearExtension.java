package com.eggmeonina.scrumble.common.config;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseClearExtension implements BeforeEachCallback {
	@Override
	public void beforeEach(ExtensionContext bean) throws Exception {
		DatabaseCleaner databaseCleaner = getDataCleaner(bean);
		databaseCleaner.execute();
	}

	// 스프링 컨텍스트의 DatabaseCleaner 빈을 가져온다.
	private DatabaseCleaner getDataCleaner(ExtensionContext extensionContext) {
		return SpringExtension.getApplicationContext(extensionContext)
			.getBean(DatabaseCleaner.class);
	}
}
