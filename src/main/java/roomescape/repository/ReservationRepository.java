package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.User;
import roomescape.dto.reservation.create.ReservationCreateRequest;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findReservations();

    Reservation createReservation(ReservationCreateRequest dto, ReservationTime time, Theme theme);

    void deleteReservation(Long id);

    List<ReservationTime> findReservedTimesByDateAndTheme(String date, Theme theme);
}
