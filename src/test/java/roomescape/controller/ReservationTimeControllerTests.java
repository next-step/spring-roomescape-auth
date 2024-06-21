package roomescape.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.controller.dto.AvailableReservationTimeResponse;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class ReservationTimeControllerTests {

	@InjectMocks
	private ReservationTimeController reservationTimeController;

	@Mock
	private ReservationTimeService reservationTimeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createReservationTime() {
		// given
		var reservationTimeRequest = new ReservationTimeRequest("10:10");
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:10");

		given(this.reservationTimeService.create(reservationTimeRequest)).willReturn(reservationTimeResponse);

		// when
		ResponseEntity<ReservationTimeResponse> responseEntity = this.reservationTimeController
			.create(reservationTimeRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isEqualTo(reservationTimeResponse);
	}

	@Test
	void getReservationTimes() {
		// given
		List<ReservationTimeResponse> reservationTimes = new ArrayList<>();

		given(this.reservationTimeService.getReservationTimes()).willReturn(reservationTimes);

		// when
		ResponseEntity<List<ReservationTimeResponse>> responseEntity = this.reservationTimeController
			.getReservationTimes();

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(reservationTimes);
	}

	@Test
	void deleteReservationTime() {
		// given
		long id = 1L;

		// when
		ResponseEntity<Void> responseEntity = this.reservationTimeController.delete(id);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(this.reservationTimeService).delete(id);
	}

	@Test
	void getAvailableReservationTimes() {
		// given
		String date = "2024-06-18";

		AvailableReservationTimeResponse firstResponse = new AvailableReservationTimeResponse(1L, "10:00", true);
		AvailableReservationTimeResponse secondResponse = new AvailableReservationTimeResponse(2L, "11:00", false);

		List<AvailableReservationTimeResponse> responses = Arrays.asList(firstResponse, secondResponse);

		given(this.reservationTimeService.getAvailableReservationTimes(anyString(), eq(1L))).willReturn(responses);

		// when
		var responseEntity = this.reservationTimeController.getAvailableReservationTimes(date, 1L);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotEmpty();
		assertThat(responseEntity.getBody()).isEqualTo(responses);
		assertThat(responseEntity.getBody().get(0).timeId()).isEqualTo(1L);
		assertThat(responseEntity.getBody().get(0).startAt()).isEqualTo("10:00");
		assertThat(responseEntity.getBody().get(0).alreadyBooked()).isEqualTo(true);
		assertThat(responseEntity.getBody().get(1).timeId()).isEqualTo(2L);
		assertThat(responseEntity.getBody().get(1).startAt()).isEqualTo("11:00");
		assertThat(responseEntity.getBody().get(1).alreadyBooked()).isEqualTo(false);

	}

}
