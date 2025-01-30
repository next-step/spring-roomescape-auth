package roomescape.domain.reservationtime.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.common.ClockHolder;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.ReservationDate;
import roomescape.domain.reservation.domain.ReservationRepository;
import roomescape.domain.reservationtime.application.response.AvailableReservationTimeResponse;
import roomescape.domain.reservationtime.domain.ReservationTime;
import roomescape.domain.reservationtime.domain.ReservationTimeId;
import roomescape.domain.reservationtime.domain.ReservationTimeRepository;
import roomescape.domain.reservationtime.exception.DupliactedReservationTimeException;
import roomescape.domain.reservationtime.exception.ReservationTimeAlreadyInUse;
import roomescape.domain.theme.domain.ThemeId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandService {

    private final ClockHolder clockHolder;
    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 특정 테마의 특정 일자에 대한 예약 가능한 시간 목록을 반환한다.
     *
     * @param date    예약 가능한 시간 목록을 조회할 날짜
     * @param themeId 예약 가능한 시간 목록을 조회할 테마 식별자
     * @return 예약 가능한 시간 목록
     */
    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(
            LocalDate date,
            Long themeId
    ) {
        List<ReservationTime> allTimes = timeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAllByThemeIdAndDate(
                new ThemeId(themeId),
                new ReservationDate(date)
        );

        Set<ReservationTimeId> reservedTimeIds = reservations.stream()
                .map(Reservation::getTimeId)
                .collect(Collectors.toSet());

        // 예약되지 않은 시간들(=이용 가능한 시간)을 추출
        List<ReservationTime> availableTimes = allTimes.stream()
                .filter(time -> !reservedTimeIds.contains(time.getId()))
                .toList();

        return AvailableReservationTimeResponse.from(availableTimes);
    }

    public ReservationTime append(final ReservationTimeAppendRequest request) {
        verifyUniqueStartAt(request.getStartAt());

        final ReservationTime time = ReservationTime.defaultOf(request.getStartAt(), clockHolder);
        return timeRepository.save(time);
    }

    public void delete(final ReservationTimeId timeId) {
        final ReservationTime time = timeRepository.getById(timeId);
        verifyTimeIdNotInUse(time);
        timeRepository.delete(time);
    }

    private void verifyTimeIdNotInUse(final ReservationTime time) {
        if (anyConfirmedReservationExistsBy(time)) {
            throw ReservationTimeAlreadyInUse.from(time);
        }
    }

    private boolean anyConfirmedReservationExistsBy(final ReservationTime time) {
        final List<Reservation> reservations = reservationRepository.findAllByTimeId(time.getId());
        return reservations.stream().anyMatch(Reservation::isConfirmed);
    }

    private void verifyUniqueStartAt(final LocalTime startAt) {
        final Optional<ReservationTime> timeOpt = timeRepository.findByStartAt(startAt);
        if (timeOpt.isPresent()) {
            final ReservationTime existing = timeRepository.getByStartAt(startAt);
            throw DupliactedReservationTimeException.from(existing);
        }
    }
}
