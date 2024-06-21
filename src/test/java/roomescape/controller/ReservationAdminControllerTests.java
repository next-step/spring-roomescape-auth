package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.controller.dto.ReservationAdminRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
		assertThat(responseEntity.getBody()).isEqualTo(reservationResponse);
	}

}
