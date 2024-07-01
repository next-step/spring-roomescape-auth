package roomescape.reservationtime.domain;

import roomescape.reservationtime.domain.entity.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();
    List<ReservationTime> findMatchWith(String date, Long themeId);
    Optional<ReservationTime> findById(Long id);
    Optional<ReservationTime> findByStartAt(String startAt);
    Long countReservationMatchWith(Long id);
    long save(ReservationTime reservationTime);
    long deleteById(Long id);
}
