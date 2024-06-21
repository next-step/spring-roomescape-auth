package roomescape.integration;

import java.util.List;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import roomescape.DataTimeFormatterUtils;
import roomescape.auth.JwtCookieManager;
import roomescape.auth.JwtTokenProvider;
import roomescape.web.controller.dto.MemberResponse;
import roomescape.web.controller.dto.ReservationRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationTimeRequest;
import roomescape.web.controller.dto.ReservationTimeResponse;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.web.controller.dto.ThemeResponse;
import roomescape.domain.MemberRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class ReservationIntegrationTests {

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@LocalServerPort
	private int port;

	@Test
	void reservationControllerEndpoints() {

		// create reservation time
		// given
		ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("10:00");

		// when
		var createReservationTime = this.restTemplate.postForEntity("http://localhost:" + this.port + "/times",
				reservationTimeRequest, ReservationTimeResponse.class);

		// then
		assertThat(createReservationTime.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		var reservationTimeResponse = createReservationTime.getBody();
		assertThat(reservationTimeResponse).isNotNull();
		assertThat(reservationTimeResponse.startAt()).isEqualTo("10:00");

		// create theme
		// given
		ThemeRequest themeRequest = new ThemeRequest("테마1", "첫번째테마", "테마이미지");

		// when
		var createTheme = this.restTemplate.postForEntity("http://localhost:" + this.port + "/themes", themeRequest,
				ThemeResponse.class);

		// then
		assertThat(createTheme.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		var themeResponse = createTheme.getBody();
		assertThat(themeResponse).isNotNull();
		assertThat(themeResponse.id()).isEqualTo(1L);
		assertThat(themeResponse.name()).isEqualTo("테마1");
		assertThat(themeResponse.description()).isEqualTo("첫번째테마");
		assertThat(themeResponse.thumbnail()).isEqualTo("테마이미지");

		// create reservation
		// given
		ReservationRequest reservationRequest = new ReservationRequest("tester",
				DataTimeFormatterUtils.getFormattedTomorrowDate(), 1L, 1L);

		MemberResponse memberResponse = new MemberResponse(1L, "tester", "tester@gmail.com", MemberRole.USER.name());
		String token = this.jwtTokenProvider.createToken(memberResponse);

		Cookie cookie = JwtCookieManager.createCookie(token, 3600);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, cookie.getName() + "=" + cookie.getValue());

		HttpEntity<ReservationRequest> requestEntity = new HttpEntity<>(reservationRequest, headers);

		// when
		var createReservation = this.restTemplate.exchange("http://localhost:" + this.port + "/reservations",
				HttpMethod.POST, requestEntity, ReservationResponse.class);

		// then
		assertThat(createReservation.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ReservationResponse reservationResponse = createReservation.getBody();
		assertThat(reservationResponse).isNotNull();
		assertThat(reservationResponse.name()).isEqualTo("tester");

		// get reservations
		// when
		var getReservations = this.restTemplate.getForEntity("http://localhost:" + this.port + "/reservations",
				List.class);

		// then
		assertThat(getReservations.getStatusCode()).isEqualTo(HttpStatus.OK);
		var reservations = getReservations.getBody();
		assertThat(reservations).isNotNull();
		assertThat(reservations.size()).isGreaterThan(0);

		// cancel reservation
		// given
		long reservationId = reservationResponse.id();

		// when
		ResponseEntity<Void> cancelResponse = this.restTemplate.exchange(
				"http://localhost:" + this.port + "/reservations/" + reservationId, HttpMethod.DELETE, null,
				Void.class);

		// then
		assertThat(cancelResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// check reservation
		getReservations = this.restTemplate.getForEntity("http://localhost:" + this.port + "/reservations", List.class);

		assertThat(getReservations.getStatusCode()).isEqualTo(HttpStatus.OK);
		reservations = getReservations.getBody();
		assertThat(reservations).isNotNull();
		assertThat(reservations.size()).isEqualTo(0);

		// delete reservation time
		// given
		long reservationTimeId = reservationTimeResponse.id();

		// when
		var deleteReservationTime = this.restTemplate.exchange(
				"http://localhost:" + this.port + "/times/" + reservationTimeId, HttpMethod.DELETE, null, Void.class);

		// then
		assertThat(deleteReservationTime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// delete theme
		// given
		long themeId = themeResponse.id();

		// when
		var deleteTheme = this.restTemplate.exchange("http://localhost:" + this.port + "/themes/" + themeId,
				HttpMethod.DELETE, null, Void.class);

		// then
		assertThat(deleteTheme.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

}
