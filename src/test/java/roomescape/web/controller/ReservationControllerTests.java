package roomescape.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.web.controller.ReservationController;
import roomescape.web.controller.dto.ReservationRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationTimeResponse;
import roomescape.web.controller.dto.ThemeResponse;
import roomescape.domain.LoginMember;
import roomescape.domain.MemberRole;
import roomescape.service.ReservationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(responseEntity.getBody()).isNotNull();
			softly.assertThat(responseEntity.getBody()).hasSize(1);

			ReservationResponse response = responseEntity.getBody().get(0);
			softly.assertThat(response.id()).isEqualTo(reservationResponse.id());
			softly.assertThat(response.name()).isEqualTo(reservationResponse.name());
			softly.assertThat(response.date()).isEqualTo(reservationResponse.date());
			softly.assertThat(response.time().id()).isEqualTo(reservationResponse.time().id());
			softly.assertThat(response.time().startAt()).isEqualTo(reservationResponse.time().startAt());
			softly.assertThat(response.theme().id()).isEqualTo(reservationResponse.theme().id());
			softly.assertThat(response.theme().name()).isEqualTo(reservationResponse.theme().name());
			softly.assertThat(response.theme().description()).isEqualTo(reservationResponse.theme().description());
			softly.assertThat(response.theme().thumbnail()).isEqualTo(reservationResponse.theme().thumbnail());
		});
	}

	@Test
	void createReservation() {
		// given
		var reservationRequest = new ReservationRequest("tester", "2024-06-06", 1L, 1L);
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:00");
		var themeResponse = new ThemeResponse(1L, "방탈출1", "첫번째테마", "테마이미지");
		var reservationResponse = new ReservationResponse(1L, "tester", "2024-06-06", reservationTimeResponse,
				themeResponse);
		var loginMember = LoginMember.builder()
			.name("tester")
			.email("tester@gmail.com")
			.role(MemberRole.USER.name())
			.build();

		given(this.reservationService.create(reservationRequest, loginMember)).willReturn(reservationResponse);

		// when
		ResponseEntity<ReservationResponse> responseEntity = this.reservationController.create(reservationRequest,
				loginMember);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(responseEntity.getBody()).isNotNull();
			softly.assertThat(responseEntity.getBody().id()).isEqualTo(reservationResponse.id());
			softly.assertThat(responseEntity.getBody().name()).isEqualTo(reservationResponse.name());
			softly.assertThat(responseEntity.getBody().date()).isEqualTo(reservationResponse.date());
			softly.assertThat(responseEntity.getBody().time().id()).isEqualTo(reservationResponse.time().id());
			softly.assertThat(responseEntity.getBody().time().startAt()).isEqualTo(reservationResponse.time().startAt());
			softly.assertThat(responseEntity.getBody().theme().id()).isEqualTo(reservationResponse.theme().id());
			softly.assertThat(responseEntity.getBody().theme().name()).isEqualTo(reservationResponse.theme().name());
			softly.assertThat(responseEntity.getBody().theme().description()).isEqualTo(reservationResponse.theme().description());
			softly.assertThat(responseEntity.getBody().theme().thumbnail()).isEqualTo(reservationResponse.theme().thumbnail());
		});
	}

	@Test
	void cancelReservation() {
		// given
		long id = 1L;

		// when
		ResponseEntity<Void> responseEntity = this.reservationController.cancel(id);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(this.reservationService).cancel(id);
	}

}
