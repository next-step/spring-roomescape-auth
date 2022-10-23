package nextstep.domain.repository;

import java.util.Optional;
import nextstep.domain.Reservation;

public interface ReservationRepository {

    void save(Reservation reservation);

    Optional<Reservation> findBy(Long scheduleId, Long memberId);

    void deleteBy(Long id);

    void deleteAll();
}
