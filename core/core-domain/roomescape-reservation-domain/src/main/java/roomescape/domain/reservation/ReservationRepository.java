package roomescape.domain.reservation;

import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;

import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void delete(ReservationId id);

    Optional<Reservation> findById(ReservationId id);

    Reservations findByDateAndThemeId(ReservationDate date, ThemeId themeId);

    ReservationViews findAllReservationViews();

    boolean existByDateAndTimeId(ReservationDate date, ReservationTimeId timeId);

    boolean existByTimeId(ReservationTimeId timeId);

}
