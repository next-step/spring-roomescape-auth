package roomescape.application.domain.reservationtime.service.component.reader;

import org.springframework.stereotype.Component;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.Reservations;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.reservationtime.ReservationTimes;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.error.exception.NotFoundDomainException;

@Component
public class ReservationTimeReader {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeReader(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime readById(ReservationTimeId id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> NotFoundDomainException.notFoundReservationTime(id));
    }

    public ReservationTimes readByDateAndThemeId(ReservationDate date, ThemeId themeId) {
        Reservations reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        return reservationTimeRepository.findExcludeById(reservations.fetchTimeIds());
    }

    public ReservationTimes readAll() {
        return reservationTimeRepository.findAll();
    }
}
