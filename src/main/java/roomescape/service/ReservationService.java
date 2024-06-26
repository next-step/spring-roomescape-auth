package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import roomescape.domain.LoginMember;
import roomescape.domain.Reservation;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.ReservationRepository;
import roomescape.web.controller.dto.CreateReservationRequest;
import roomescape.web.controller.dto.ReservationAdminRequest;
import roomescape.web.controller.dto.ReservationRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationSearchRequest;

import org.springframework.stereotype.Service;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;

	private final ReservationTimeService reservationTimeService;

	private final ThemeService themeService;

	private final MemberService memberService;

	ReservationService(ReservationRepository reservationRepository, ReservationTimeService reservationTimeService,
			ThemeService themeService, MemberService memberService) {
		this.reservationRepository = reservationRepository;
		this.reservationTimeService = reservationTimeService;
		this.themeService = themeService;
		this.memberService = memberService;
	}

	public List<ReservationResponse> getReservations() {
		return this.reservationRepository.findAll().stream().map(ReservationResponse::from).toList();
	}

	public ReservationResponse create(ReservationRequest request, LoginMember loginMember) {
		var createReservationRequest = CreateReservationRequest.builder()
			.date(request.date())
			.timeId(request.timeId())
			.themeId(request.themeId())
			.memberName(loginMember.getName())
			.build();
		return createReservation(createReservationRequest);
	}

	public ReservationResponse createByAdmin(ReservationAdminRequest request) {
		var foundMember = this.memberService.findById(request.memberId());
		var createReservationRequest = CreateReservationRequest.builder()
			.date(request.date())
			.timeId(request.timeId())
			.themeId(request.themeId())
			.memberName(foundMember.getName())
			.build();
		return createReservation(createReservationRequest);
	}

	public void cancel(long id) {
		var isExist = this.reservationRepository.isExistId(id);
		if (!isExist) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_RESERVATION);
		}
		this.reservationRepository.delete(id);
	}

	private ReservationResponse createReservation(CreateReservationRequest createReservationRequest) {
		var reservationTime = this.reservationTimeService.getReservationTimeById(createReservationRequest.getTimeId());
		var date = createReservationRequest.getDate();
		var themeId = createReservationRequest.getThemeId();

		checkReservationAvailability(date, reservationTime.getStartAt(), themeId);

		var theme = this.themeService.getThemeById(themeId);
		var reservation = Reservation.builder()
			.name(createReservationRequest.getMemberName())
			.date(date)
			.time(reservationTime)
			.theme(theme)
			.build();
		var savedReservation = this.reservationRepository.save(reservation);
		return ReservationResponse.from(savedReservation, reservationTime, theme);
	}

	private void checkReservationAvailability(String date, String time, long themeId) {
		LocalDate reservationDate = LocalDate.parse(date);
		LocalTime reservationTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
		if (reservationDate.isBefore(LocalDate.now())
				|| (reservationDate.isEqual(LocalDate.now()) && reservationTime.isBefore(LocalTime.now()))) {
			throw new RoomEscapeException(ErrorCode.PAST_RESERVATION);
		}

		if (this.reservationRepository.isDuplicateReservation(date, themeId)) {
			throw new RoomEscapeException(ErrorCode.DUPLICATE_RESERVATION);
		}
	}

	public List<ReservationResponse> searchReservations(ReservationSearchRequest request) {
		return ReservationResponse.from(this.reservationRepository.findReservations(request.memberId(),
				request.themeId(), request.dateFrom(), request.dateTo()));
	}

}
