package roomescape.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.web.controller.dto.LoginResponse;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.domain.MemberRole;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class LoginIntegrationTests {

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@LocalServerPort
	private int port;

	private HttpHeaders headers;

	@BeforeEach
	void setUp() {
		this.headers = new HttpHeaders();
	}

	@Test
	void login() {
		// given
		var loginRequest = new LoginRequest("tester@gmail.com", "1234");

		// when
		var loginResponse = this.restTemplate.postForEntity("http://localhost:" + this.port + "/login", loginRequest,
				Void.class);

		// then
		assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	void loginUnauthorized() {
		// given
		var invalidLoginRequest = new LoginRequest("tester@gmail.com", "wrongpassword");

		// when
		var invalidLoginResponse = this.restTemplate.postForEntity("http://localhost:" + this.port + "/login",
				invalidLoginRequest, Void.class);

		// then
		assertThat(invalidLoginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void checkLoginStatus() {
		// given
		var loginRequest = new LoginRequest("tester@gmail.com", "1234");

		// when
		var loginResponse = this.restTemplate.postForEntity("http://localhost:" + this.port + "/login", loginRequest,
				Void.class);

		assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		String token = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

		this.headers.set(HttpHeaders.COOKIE, token);

		// login check
		// when
		var checkResponse = this.restTemplate.exchange("http://localhost:" + this.port + "/login/check", HttpMethod.GET,
				new HttpEntity<>(this.headers), LoginResponse.class);

		// then
		assertThat(checkResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(checkResponse.getBody()).isNotNull();
		assertThat(checkResponse.getBody().role()).isEqualTo(MemberRole.USER.name());
	}

	@Test
	void logout() {
		// given
		var loginRequest = new LoginRequest("tester@gmail.com", "1234");
		this.restTemplate.postForEntity("http://localhost:" + this.port + "/login", loginRequest, Void.class);

		// when
		var logoutResponse = this.restTemplate.postForEntity("http://localhost:" + this.port + "/logout", null,
				Void.class);

		// then
		assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// login check
		// when
		var checkResponse = this.restTemplate.getForEntity("http://localhost:" + this.port + "/login/check",
				Void.class);

		// then
		assertThat(checkResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

}
