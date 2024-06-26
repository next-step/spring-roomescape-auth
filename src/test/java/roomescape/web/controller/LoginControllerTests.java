package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.web.controller.dto.LoginResponse;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoginControllerTests {

	@InjectMocks
	private LoginController loginController;

	@Mock
	private AuthService authService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void login() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", "1234");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		String mockToken = "mock-token";

		given(this.authService.generateLoginToken(any(LoginRequest.class))).willReturn(mockToken);

		// when
		ResponseEntity<LoginResponse> responseEntity = this.loginController.login(loginRequest, mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNull();
	}

	@Test
	void loginNotFoundMember() {
		// given
		LoginRequest loginRequest = new LoginRequest("notfoundmember@gmail.com", "1234");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		given(this.authService.generateLoginToken(any(LoginRequest.class)))
			.willThrow(new RoomEscapeException(ErrorCode.NOT_FOUND_MEMBER));

		// when, then
		assertThatThrownBy(() -> this.loginController.login(loginRequest, mockResponse))
			.isInstanceOf(RoomEscapeException.class)
			.hasMessage(ErrorCode.NOT_FOUND_MEMBER.getMessage());
	}

	@Test
	void loginUnauthorized() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", "wrong-password");
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		given(this.authService.generateLoginToken(any(LoginRequest.class)))
			.willThrow(new RoomEscapeException(ErrorCode.INVALID_PASSWORD));

		// when, then
		assertThatThrownBy(() -> this.loginController.login(loginRequest, mockResponse))
			.isInstanceOf(RoomEscapeException.class)
			.hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
	}

	@Test
	void checkLoginSuccess() {
		// given
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		LoginResponse loginResponse = new LoginResponse("USER");

		Cookie cookie = new Cookie("token", "mock-token");
		given(mockRequest.getCookies()).willReturn(new Cookie[] { cookie });

		given(this.authService.findRoleByToken(any())).willReturn(loginResponse);

		// when
		ResponseEntity<LoginResponse> responseEntity = this.loginController.check(mockRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(loginResponse);
	}

	@Test
	void logout() {
		// given
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		// when
		ResponseEntity<Void> responseEntity = this.loginController.logout(mockResponse);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(mockResponse.getCookies()).isNotNull();
		assertThat(mockResponse.getCookies()).hasSize(1);
		assertThat(mockResponse.getCookies()[0].getName()).isEqualTo("token");
		assertThat(mockResponse.getCookies()[0].getValue()).isNull();
		assertThat(mockResponse.getCookies()[0].getMaxAge()).isZero();
	}

}
