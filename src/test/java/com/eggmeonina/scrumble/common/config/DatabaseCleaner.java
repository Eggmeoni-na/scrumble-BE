package com.eggmeonina.scrumble.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DatabaseCleaner {

	// 테이블 이름들을 저장할 List
	private final List<String> tableNames = new ArrayList<>();

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void findDatabaseTableNames() {
		List<Object[]> tableInfos = em.createNativeQuery("SHOW TABLES").getResultList();
		for (Object[] tableInfo : tableInfos) {
			String tableName = (String) tableInfo[0];
			tableNames.add(tableName);
		}
	}

	private void truncate() {
		// 외래 키 체크 비활성화
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
		for (String tableName : tableNames) {
			// TRUNCATE
			em.createNativeQuery(String.format("TRUNCATE TABLE %s", tableName)).executeUpdate();
		}
		// 외래 키 체크 활성화
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

	}

	@Transactional
	public void execute(){
		em.clear();
		truncate();
	}
}
