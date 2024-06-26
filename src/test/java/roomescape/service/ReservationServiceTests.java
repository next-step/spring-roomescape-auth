package roomescape.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.DataTimeFormatterUtils;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.web.controller.dto.ReservationAdminRequest;
import roomescape.web.controller.dto.ReservationRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ReservationServiceTests {

	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private ReservationTimeService reservationTimeService;

	@Mock
	private ThemeService themeService;

	@Mock
	private MemberService memberService;

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private ReservationTimeRepository reservationTimeRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getReservations() {
		// given
		List<Reservation> reservations = new ArrayList<>();

		ReservationTime reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();
		Theme theme = Theme.builder().id(1L).name("테마1").description("첫번째테마").thumbnail("테마이미지").build();

		Reservation reservation = Reservation.builder()
			.id(1L)
			.name("tester")
			.date("2024-06-06")
			.time(reservationTime)
			.theme(theme)
			.build();
		reservations.add(reservation);

		given(this.reservationRepository.findAll()).willReturn(reservations);

		// when
		var resultReservations = this.reservationService.getReservations();

		// then
		assertThat(resultReservations).isNotEmpty();
		assertThat(resultReservations).hasSize(1);
		assertThat(resultReservations).allSatisfy((reservationResponse) -> {
			assertThat(reservationResponse.id()).isEqualTo(1L);
			assertThat(reservationResponse.name()).isEqualTo("tester");
			assertThat(reservationResponse.date()).isEqualTo("2024-06-06");
			assertThat(reservationResponse.time().id()).isEqualTo(1L);
			assertThat(reservationResponse.time().startAt()).isEqualTo("10:00");
			assertThat(reservationResponse.theme().id()).isEqualTo(1L);
			assertThat(reservationResponse.theme().name()).isEqualTo("테마1");
			assertThat(reservationResponse.theme().description()).isEqualTo("첫번째테마");
			assertThat(reservationResponse.theme().thumbnail()).isEqualTo("테마이미지");
		});
	}

	@Test
	void createReservation() {
		// given
		ReservationRequest request = new ReservationRequest("tester", DataTimeFormatterUtils.getFormattedTomorrowDate(),
				1L, 1L);

		ReservationTime reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();

		Reservation reservation = Reservation.builder()
			.id(1L)
			.name("tester")
			.date("2024-06-06")
			.time(reservationTime)
			.build();

		Theme theme = Theme.builder().id(1L).name("테마1").description("첫번째테마").thumbnail("테마이미지").build();

		given(this.themeService.getThemeById(1L)).willReturn(theme);
		given(this.reservationTimeService.getReservationTimeById(1L)).willReturn(reservationTime);
		given(this.reservationTimeRepository.findById(1L)).willReturn(reservationTime);
		given(this.reservationRepository.save(any(Reservation.class))).willReturn(reservation);

		var loginMember = LoginMember.builder()
			.name("tester")
			.email("tester@gmail.com")
			.role(MemberRole.USER.name())
			.build();

		// when
		var createdReservation = this.reservationService.create(request, loginMember);

		// then
		assertThat(createdReservation).isNotNull();
		assertThat(createdReservation.id()).isEqualTo(1L);
		assertThat(createdReservation.name()).isEqualTo("tester");
		assertThat(createdReservation.date()).isEqualTo("2024-06-06");
		assertThat(createdReservation.time()).isNotNull();
		assertThat(createdReservation.time().id()).isEqualTo(1L);
		assertThat(createdReservation.time().startAt()).isEqualTo("10:00");
		assertThat(createdReservation.theme()).isNotNull();
		assertThat(createdReservation.theme().id()).isEqualTo(1L);
		assertThat(createdReservation.theme().name()).isEqualTo("테마1");
		assertThat(createdReservation.theme().description()).isEqualTo("첫번째테마");
		assertThat(createdReservation.theme().thumbnail()).isEqualTo("테마이미지");
	}

	@Test
	void createReservationByAdmin() {
		// given

		ReservationAdminRequest request = new ReservationAdminRequest("예약자이름", "2024-06-30", 1L, 1L, 1L);

		var member = Member.builder().id(1L).name("예약자이름").email("admin@nextstep.com").role("ADMIN").build();

		var reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();

		var theme = Theme.builder().id(1L).name("테마1").description("테마 설명").thumbnail("테마 이미지").build();

		given(this.memberService.findById(1L)).willReturn(member);
		given(this.reservationTimeService.getReservationTimeById(1L)).willReturn(reservationTime);
		given(this.themeService.getThemeById(1L)).willReturn(theme);
		given(this.reservationRepository.save(any())).willAnswer((invocation) -> {
			Reservation reservation = invocation.getArgument(0);
			reservation.setId(1L);
			return reservation;
		});

		// when
		var createdReservation = this.reservationService.createByAdmin(request);

		// then
		assertThat(createdReservation).isNotNull();
		assertThat(createdReservation.name()).isEqualTo("예약자이름");
		assertThat(createdReservation.date()).isEqualTo("2024-06-30");
		assertThat(createdReservation.time().id()).isEqualTo(1L);
		assertThat(createdReservation.theme().id()).isEqualTo(1L);
	}

	@Test
	void cancelReservationException() {
		// given
		long id = 1L;

		// when, then
		assertThatThrownBy(() -> this.reservationService.cancel(id)).isInstanceOf(RoomEscapeException.class)
			.hasMessage(ErrorCode.NOT_FOUND_RESERVATION.getMessage());
	}

	@Test
	void searchReservations() {
		// given
		long memberId = 1L;
		long themeId = 1L;
		String dateFrom = "2024-06-01";
		String dateTo = "2024-06-30";

		ReservationTime reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();
		Theme theme = Theme.builder().id(1L).name("테마1").description("첫번째테마").thumbnail("테마이미지").build();

		Reservation reservation = Reservation.builder()
			.id(1L)
			.name("tester")
			.date("2024-06-06")
			.time(reservationTime)
			.theme(theme)
			.build();

		List<Reservation> reservations = List.of(reservation);

		ReservationSearchRequest request = new ReservationSearchRequest(memberId, themeId, dateFrom, dateTo);
		given(this.reservationRepository.findReservations(anyLong(), anyLong(), anyString(), anyString()))
			.willReturn(reservations);

		// when
		List<ReservationResponse> searchReservations = this.reservationService.searchReservations(request);

		// then
		assertThat(searchReservations).hasSize(1);
		assertThat(searchReservations).allSatisfy((resultReservation) -> {
			assertThat(resultReservation.name()).isEqualTo("tester");
			assertThat(resultReservation.theme().id()).isEqualTo(1L);
			assertThat(resultReservation.date()).isBetween("2024-06-01", "2024-06-30");
		});
	}

	@Test
	void searchReservationsEmptyResult() {

		// given
		long memberId = 1L;
		long themeId = 1L;
		String dateFrom = "2024-06-01";
		String dateTo = "2024-06-30";

		ReservationSearchRequest request = new ReservationSearchRequest(memberId, themeId, dateFrom, dateTo);

		given(this.reservationRepository.findReservations(anyLong(), anyLong(), anyString(), anyString()))
				.willReturn(Collections.emptyList());

		// when
		List<ReservationResponse> searchReservations = this.reservationService.searchReservations(request);

		// then
		assertThat(searchReservations).isEmpty();
	}

}
