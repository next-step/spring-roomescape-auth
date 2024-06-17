package roomescape.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.service.ReservationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class ReservationControllerTests {

	@InjectMocks
	private ReservationController reservationController;

	@Mock
	private ReservationService reservationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getReservations() {
		// given
		List<ReservationResponse> reservations = new ArrayList<>();
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:10");
		var themeResponse = new ThemeResponse(1L, "방탈출1", "첫번째테마", "테마이미지");
		var reservationResponse = new ReservationResponse(1L, "tester", "2024-06-06", reservationTimeResponse,
				themeResponse);
		reservations.add(reservationResponse);

		given(this.reservationService.getReservations()).willReturn(reservations);

		// when
		ResponseEntity<List<ReservationResponse>> responseEntity = this.reservationController.getReservations();

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(reservations);
	}

	@Test
	void createReservation() {
		// given
		var reservationRequest = new ReservationRequest("tester", "2024-06-06", 1L, 1L);
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:00");
		var themeResponse = new ThemeResponse(1L, "방탈출1", "첫번째테마", "테마이미지");
		var reservationResponse = new ReservationResponse(1L, "tester", "2024-06-06", reservationTimeResponse,
				themeResponse);

		given(this.reservationService.create(reservationRequest)).willReturn(reservationResponse);

		// when
		ResponseEntity<ReservationResponse> responseEntity = this.reservationController.create(reservationRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(reservationResponse);
	}

	@Test
	void cancelReservation() {
		// given
		long id = 1L;

		// when
		ResponseEntity<Void> responseEntity = this.reservationController.cancel(id);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(this.reservationService).cancel(id);
	}

}