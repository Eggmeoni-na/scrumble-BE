package com.eggmeonina.scrumble.domain.membership.facade;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.membership.domain.Membership;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.repository.MembershipRepository;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class MembershipFacadeServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired private MembershipFacadeService membershipFacadeService;
	@Autowired private SquadRepository squadRepository;
	@Autowired private MemberRepository memberRepository;
	@Autowired private MembershipRepository membershipRepository;

	@Test
	@DisplayName("스쿼드가 정상으로 생성되면 스쿼드 번호를 반환한다")
	void createSquad_success_returnsSquadId() {
		// given
		Member newMember = Member.create()
			.email("test@test.com")
			.name("testA")
			.memberStatus(MemberStatus.JOIN)
			.oauthInformation(new OauthInformation("123456", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();

		SquadCreateRequest request = new SquadCreateRequest("테스트 스쿼드");

		memberRepository.save(newMember);

		// when
		Long squadId = membershipFacadeService.createSquad(newMember.getId(), request);

		Squad foundSquad = squadRepository.findById(squadId).get();
		Optional<Membership> foundMembership = membershipRepository.findByMemberIdAndSquadId(newMember.getId(),
			squadId);

		// then - 스쿼드와 멤버십이 정상으로 생성된다
		assertThat(squadId).isEqualTo(foundSquad.getId());
		assertThat(foundMembership).isNotEmpty();
	}

	@Test
	@DisplayName("멤버십이 생성되지 않으면 스쿼드도 생성되지 않는다")
	void createSquad_fail_DontCreateSquad() {
		// given
		Long tempMemberId = 1L;
		SquadCreateRequest request = new SquadCreateRequest("테스트 스쿼드");

		// when, then
		// 예외가 발생하면 데이터가 rollback된다.
		assertThatThrownBy(()->membershipFacadeService.createSquad(tempMemberId, request))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());

		long squadCount = squadRepository.count();

		// 스쿼드 데이터 초기화 여부를 확인한다.
		assertThat(squadCount).isZero();
	}

}
