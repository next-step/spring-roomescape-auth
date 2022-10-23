package nextstep.domain.reservation.usecase;

import nextstep.domain.reservation.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
     Long save(Reservation reservation);
     Optional<Reservation> findById(Long id);
     Optional<Reservation> findByScheduleId(Long scheduleId);
     List<Reservation> findByMemberName(String name);
     List<Reservation> findAllBy(String date);
     void delete(Long id);
}
