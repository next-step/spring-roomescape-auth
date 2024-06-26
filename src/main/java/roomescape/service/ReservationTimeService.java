package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;

import roomescape.domain.ReservationTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.web.controller.dto.AvailableReservationTimeResponse;
import roomescape.web.controller.dto.ReservationTimeRequest;
import roomescape.web.controller.dto.ReservationTimeResponse;

import org.springframework.stereotype.Service;

@Service
public class ReservationTimeService {

	private final ReservationTimeRepository reservationTimeRepository;

	ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
		this.reservationTimeRepository = reservationTimeRepository;
	}

	public ReservationTimeResponse create(ReservationTimeRequest request) {
		var reservationTime = ReservationTime.builder().startAt(request.startAt()).build();
		var savedReservationTime = this.reservationTimeRepository.save(reservationTime);
		return ReservationTimeResponse.from(savedReservationTime);
	}

	public List<ReservationTimeResponse> getReservationTimes() {
		return this.reservationTimeRepository.findAll()
			.stream()
			.map(ReservationTimeResponse::from)
			.collect(Collectors.toList());
	}

	public void delete(long id) {
		var isExist = this.reservationTimeRepository.isExistId(id);
		if (!isExist) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
		}
		this.reservationTimeRepository.delete(id);
	}

	public ReservationTime getReservationTimeById(long id) {
		var foundReservationTime = this.reservationTimeRepository.findById(id);

		if (foundReservationTime == null) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
		}
		return foundReservationTime;
	}

	public List<AvailableReservationTimeResponse> getAvailableReservationTimes(String date, long themeId) {
		var reservationTimes = this.reservationTimeRepository.findAll();
		var reservedTimeIds = this.reservationTimeRepository.findReservedTimeIds(date, themeId);

		return reservationTimes.stream()
			.map((reservationTime) -> AvailableReservationTimeResponse.from(reservationTime,
					reservedTimeIds.contains(reservationTime.getId())))
			.collect(Collectors.toList());
	}

}
