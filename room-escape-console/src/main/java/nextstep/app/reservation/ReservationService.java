package nextstep.app.reservation;

import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.reservation.exception.DuplicationReservationException;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void save(Long scheduleId, String name) {
        reservationRepository.findByScheduleId(scheduleId).ifPresent((reservation -> {
            throw new DuplicationReservationException();
        }));

        Reservation reservation = new Reservation(
                null,
                Schedule.empty(),
                LocalDateTime.now(),
                name);

        reservationRepository.save(reservation);
    }

    public List<Reservation> findAllBy(String date) {
        return reservationRepository.findAllBy(date);
    }

    public void delete(Long reservationId) {
        reservationRepository.delete(reservationId);
    }
}
