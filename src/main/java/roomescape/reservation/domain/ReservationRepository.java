package roomescape.reservation.domain;

import roomescape.reservation.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);
    Long countMatchWith(String date, Long timeId, Long themeId);
    long save(Long memberId, String date, Long timeId, Long themeId);
    long deleteById(Long id);
}
