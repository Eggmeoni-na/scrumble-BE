package com.eggmeonina.scrumble.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eggmeonina.scrumble.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		  FROM Member m
		 WHERE m.memberStatus = 'JOIN' AND m.oauthInformation.oauthId = :oauthId
		""")
	Optional<Member> findByOauthId(final String oauthId);
}
