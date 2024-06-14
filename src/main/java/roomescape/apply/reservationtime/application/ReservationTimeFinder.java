package roomescape.apply.reservationtime.application;

import org.springframework.stereotype.Service;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.support.checker.ReservationTimeRequestChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationTimeFinder {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeFinder(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .sorted(ReservationTime::compareByStartTime)
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime findOneById(long timeId) {
        return reservationTimeRepository.findById(timeId).orElseThrow(NotFoundReservationTimeException::new);
    }

    public List<AvailableReservationTimeResponse> findAvailableTimesBy(String date, String themeId) {
        ReservationTimeRequestChecker.validateRequestParam(date, themeId);

        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        final List<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(date, Long.parseLong(themeId));
        final Set<Long> idsSet = new HashSet<>(reservedTimeIds);

        return reservationTimes.stream()
                .map(it -> AvailableReservationTimeResponse.from(it, idsSet.contains(it.getId())))
                .toList();
    }
}
