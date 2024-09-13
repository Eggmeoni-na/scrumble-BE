package com.eggmeonina.scrumble.common.resolver;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.BDDMockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;

@ExtendWith(MockitoExtension.class)
class MemberArgumentResolverTest {

	private HandlerMethodArgumentResolver memberArgumentResolver;
	private Class<?> clazz;

	private NativeWebRequest webRequest;
	@BeforeEach
	void setUp() {
		memberArgumentResolver = new MemberArgumentResolver();
		clazz = ArgumentResolverTest.class;
		webRequest = Mockito.mock();
	}

	@Test
	@DisplayName("Member 어노테이션이 선언되어 있고 LoginMember 객체를 사용하면 true를 반환한다")
	void supportsParameter_success_returnTrue() throws NoSuchMethodException {
		// given
		MethodParameter methodParameter = getMethodParameter("hasAnnotationAndObject", LoginMember.class);

		// when
		boolean actual = memberArgumentResolver.supportsParameter(methodParameter);

		// then
		assertThat(actual).isTrue();
	}

	@Test
	@DisplayName("Member 어노테이션만 선언되어 있으면 false를 반환한다")
	void supportsParameter_fail_returnFalse() throws NoSuchMethodException {
		// given
		MethodParameter methodParameter = getMethodParameter("hasAnnotationWithoutLoginMember", Object.class);

		// when
		boolean actual = memberArgumentResolver.supportsParameter(methodParameter);

		// then
		assertThat(actual).isFalse();
	}

	@Test
	@DisplayName("LoginMember 객체만 선언되어 있으면 false를 반환한다")
	void supportsParameterWithoutAnnotation_fail_returnFalse() throws NoSuchMethodException {
		// given
		MethodParameter methodParameter = getMethodParameter("hasLoginMemberWithoutAnnotation", LoginMember.class);

		// when
		boolean actual = memberArgumentResolver.supportsParameter(methodParameter);

		// then
		assertThat(actual).isFalse();
	}

	@Test
	@DisplayName("세션 데이터가 있으면 객체를 반환한다")
	void resolveArgument_success_returnsObject() throws Exception {
		// given
		// MockHttpSession 생성
		LoginMember loginMember = new LoginMember(1L, "testUser@test.com", "testA");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session); // 요청에 세션 설정
		session.setAttribute(SessionKey.LOGIN_USER.name(), loginMember);

		given(webRequest.getNativeRequest()).willReturn(request);


		// when
		LoginMember response
			= (LoginMember)memberArgumentResolver.resolveArgument(null, null, webRequest, null);

		// then
		assertSoftly(softly -> {
			softly.assertThat(response.getMemberId()).isEqualTo(loginMember.getMemberId());
			softly.assertThat(response.getEmail()).isEqualTo(loginMember.getEmail());
			softly.assertThat(response.getName()).isEqualTo(loginMember.getName());
		});
	}

	@Test
	@DisplayName("세션 데이터가 없으면 예외를 발생시킨다")
	void resolveArgument_fail_throwsException() {
		// given
		// MockHttpSession 생성
		MockHttpServletRequest request = new MockHttpServletRequest();

		given(webRequest.getNativeRequest()).willReturn(request);


		// when, then
		thenThrownBy(
			()-> memberArgumentResolver.resolveArgument(null, null, webRequest, null))
			.isInstanceOf(RuntimeException.class);
	}

	private MethodParameter getMethodParameter(String methodName, Class<?> parameter) throws NoSuchMethodException {
		Method hasAnnotationAndObject = clazz.getDeclaredMethod(methodName, parameter);
		return new MethodParameter(hasAnnotationAndObject, 0);
	}

	public static class ArgumentResolverTest{
		public void hasAnnotationAndObject(@Member LoginMember member){}
		public void hasAnnotationWithoutLoginMember(@Member Object member){}
		public void hasLoginMemberWithoutAnnotation(LoginMember member){ }
	}

}
