package roomescape.repository.domain.reservation;

import roomescape.repository.domain.reservation.entity.ReservationEntity;
import roomescape.repository.domain.reservation.projection.ReservationViewProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationJdbcRepository {

    ReservationEntity save(ReservationEntity entity);

    List<ReservationEntity> findAll();

    void delete(Long id);

    Optional<ReservationEntity> findById(Long id);

    List<ReservationEntity> findByDateAndThemeId(LocalDate date, Long themeId);

    List<ReservationViewProjection> findAllReservationViewProjection();

    Optional<ReservationEntity> findByDateAndTimeId(LocalDate date, Long timeId);

    Optional<ReservationEntity> findByTimeId(Long timeId);
}
