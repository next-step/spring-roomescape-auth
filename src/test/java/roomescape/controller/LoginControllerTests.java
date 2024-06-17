package roomescape.controller;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.auth.JwtTokenProvider;
import roomescape.controller.dto.LoginCheckResponse;
import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.MemberService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoginControllerTests {

	@InjectMocks
	private LoginController loginController;

	@Mock
	private MemberService memberService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void login() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", "1234");
		MemberResponse memberResponse = new MemberResponse(1L, "tester", "tester@gmail.com", "USER");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		String mockToken = "mock-token";

		given(this.memberService.findMemberByLoginRequest(any(LoginRequest.class))).willReturn(memberResponse);
		given(this.jwtTokenProvider.createToken(any(MemberResponse.class))).willReturn(mockToken);

		// when
		ResponseEntity<Void> responseEntity = this.loginController.login(loginRequest, mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNull();
	}

	@Test
	void loginNotFoundMember() {
		// given
		LoginRequest loginRequest = new LoginRequest("notfoundmember@gmail.com", "1234");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		given(this.memberService.findMemberByLoginRequest(any(LoginRequest.class)))
			.willThrow(new RoomEscapeException(ErrorCode.NOT_FOUND_MEMBER));

		// when
		ResponseEntity<Void> responseEntity = this.loginController.login(loginRequest, mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(responseEntity.getBody()).isNull();
	}

	@Test
	void loginUnauthorized() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", "wrong-password");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		given(this.memberService.findMemberByLoginRequest(any(LoginRequest.class)))
			.willThrow(new RoomEscapeException(ErrorCode.INVALID_PASSWORD));

		// when
		ResponseEntity<Void> responseEntity = this.loginController.login(loginRequest, mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(responseEntity.getBody()).isNull();
	}

	@Test
	void checkLoginSuccess() {
		// given
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		LoginCheckResponse loginCheckResponse = new LoginCheckResponse("USER");

		Cookie cookie = new Cookie("token", "mock-token");
		given(mockRequest.getCookies()).willReturn(new Cookie[] { cookie });

		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("role", "USER");
		Claims claims = new DefaultClaims(claimsMap);

		given(this.jwtTokenProvider.validateToken("mock-token")).willReturn(claims);

		// when
		ResponseEntity<LoginCheckResponse> responseEntity = this.loginController.check(mockRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(loginCheckResponse);
	}

	@Test
	void logout() {
		// given
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		// when
		ResponseEntity<Void> responseEntity = this.loginController.logout(mockRequest, mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(mockResponse.getCookies()).isNotNull();
		assertThat(mockResponse.getCookies()).hasSize(1);
		assertThat(mockResponse.getCookies()[0].getName()).isEqualTo("token");
		assertThat(mockResponse.getCookies()[0].getValue()).isEmpty();
		assertThat(mockResponse.getCookies()[0].getMaxAge()).isZero();
	}

}
