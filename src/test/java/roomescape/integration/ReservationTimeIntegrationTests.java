package roomescape.integration;

import java.util.List;

import org.junit.jupiter.api.Test;
import roomescape.web.controller.dto.ReservationTimeRequest;
import roomescape.web.controller.dto.ReservationTimeResponse;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class ReservationTimeIntegrationTests {

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@LocalServerPort
	private int port;

	@Test
	void reservationTimeControllerEndpoints() {
		// create reservation time
		// given
		ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("10:10");

		// when
		ResponseEntity<ReservationTimeResponse> createReservationTime = this.restTemplate.postForEntity(
				"http://localhost:" + this.port + "/times", reservationTimeRequest, ReservationTimeResponse.class);

		// then
		assertThat(createReservationTime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ReservationTimeResponse reservationTimeResponse = createReservationTime.getBody();
		assertThat(reservationTimeResponse).isNotNull();
		assertThat(reservationTimeResponse.startAt()).isEqualTo("10:10");

		// get reservation times
		// when
		var getReservationTimes = this.restTemplate.getForEntity("http://localhost:" + this.port + "/times",
				List.class);

		// then
		assertThat(getReservationTimes.getStatusCode()).isEqualTo(HttpStatus.OK);
		var reservationTimes = getReservationTimes.getBody();
		assertThat(reservationTimes).isNotNull();
		assertThat(reservationTimes.size()).isGreaterThan(0);

		// delete reservation time
		// given
		long reservationTimeId = reservationTimeResponse.id();

		// when
		var deleteReservationTime = this.restTemplate.exchange(
				"http://localhost:" + this.port + "/times/" + reservationTimeId, HttpMethod.DELETE, null, Void.class);

		// then
		assertThat(deleteReservationTime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// check reservation time
		getReservationTimes = this.restTemplate.getForEntity("http://localhost:" + this.port + "/times", List.class);

		// then
		assertThat(getReservationTimes.getStatusCode()).isEqualTo(HttpStatus.OK);
		reservationTimes = getReservationTimes.getBody();
		assertThat(reservationTimes).isNotNull();
		assertThat(reservationTimes.size()).isLessThan(1);

	}

}
