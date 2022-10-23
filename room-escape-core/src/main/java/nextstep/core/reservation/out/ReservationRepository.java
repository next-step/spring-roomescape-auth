package nextstep.core.reservation.out;

import nextstep.core.reservation.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAllByDate(LocalDate date);

    void deleteByDateAndTime(Long scheduleId, LocalDate date, LocalTime time);

    boolean existsById(Long scheduleId, LocalDate date, LocalTime time);

    void deleteById(Long reservationId);

    boolean existsById(Long reservationId);
}
