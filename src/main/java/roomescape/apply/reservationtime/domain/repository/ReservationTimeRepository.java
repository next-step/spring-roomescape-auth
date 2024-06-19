package roomescape.apply.reservationtime.domain.repository;

import roomescape.apply.reservationtime.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<Long> findIdById(long id);

    Optional<ReservationTime> findOneById(long timeId);

    List<Long> findReservedTimeIds(String date, long themeId);
}
