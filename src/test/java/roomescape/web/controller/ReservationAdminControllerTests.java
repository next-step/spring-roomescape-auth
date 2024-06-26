package roomescape.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.web.controller.dto.ReservationAdminRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationSearchRequest;
import roomescape.web.controller.dto.ReservationTimeResponse;
import roomescape.web.controller.dto.ThemeResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class ReservationAdminControllerTests {

	@InjectMocks
	private ReservationAdminController reservationAdminController;

	@Mock
	private ReservationService reservationService;

	@Mock
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	void createByAdmin() {
		// given
		var reservationAdminRequest = new ReservationAdminRequest("tester", "2024-06-06", 1L, 1L, 1L);
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:00");
		var themeResponse = new ThemeResponse(1L, "방탈출1", "첫번째테마", "테마이미지");
		var reservationResponse = new ReservationResponse(1L, "tester", "2024-06-06", reservationTimeResponse,
				themeResponse);
		var member = Member.builder()
			.id(1L)
			.name("tester")
			.email("tester@gmail.com")
			.role(MemberRole.USER.name())
			.build();

		given(this.memberService.findById(1L)).willReturn(member);
		given(this.reservationService.createByAdmin(reservationAdminRequest)).willReturn(reservationResponse);

		// when
		ResponseEntity<ReservationResponse> responseEntity = this.reservationAdminController
			.createByAdmin(reservationAdminRequest);

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
	void searchReservations() {
		// given
		var searchRequest = new ReservationSearchRequest(1L, 1L, "2024-06-01", "2024-06-30");
		var reservationTimeResponse = new ReservationTimeResponse(1L, "10:00");
		var themeResponse = new ThemeResponse(1L, "방탈출1", "첫번째테마", "테마이미지");
		var reservationResponse = new ReservationResponse(1L, "tester", "2024-06-06", reservationTimeResponse,
				themeResponse);
		List<ReservationResponse> reservationResponses = Collections.singletonList(reservationResponse);

		given(this.reservationService.searchReservations(searchRequest)).willReturn(reservationResponses);

		// when
		ResponseEntity<List<ReservationResponse>> responseEntity = this.reservationAdminController
			.searchReservations(searchRequest);

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

}
