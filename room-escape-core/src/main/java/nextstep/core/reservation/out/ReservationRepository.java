package nextstep.core.reservation.out;

import nextstep.core.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAllByScheduleIdAndDate(Long scheduleId, LocalDate date);

    void deleteById(Long reservationId);

    boolean existsById(Long reservationId);
}
