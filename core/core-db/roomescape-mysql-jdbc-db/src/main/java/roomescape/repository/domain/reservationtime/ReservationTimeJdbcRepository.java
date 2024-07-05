package roomescape.repository.domain.reservationtime;

import roomescape.repository.domain.reservationtime.entity.ReservationTimeEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeJdbcRepository {

    ReservationTimeEntity save(ReservationTimeEntity entity);

    Optional<ReservationTimeEntity> findById(Long id);

    Optional<ReservationTimeEntity> findByStartAt(LocalTime startAt);

    List<ReservationTimeEntity> findAll();

    void delete(Long id);

    List<ReservationTimeEntity> findExcludeById(List<Long> ids);
}
