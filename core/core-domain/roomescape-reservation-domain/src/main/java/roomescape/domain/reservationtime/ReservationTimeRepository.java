package roomescape.domain.reservationtime;

import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(ReservationTimeId timeId);

    boolean existByStartAt(ReservationTimeStartAt startAt);

    ReservationTimes findAll();

    void delete(ReservationTimeId id);

    ReservationTimes findExcludeById(List<ReservationTimeId> ids);
}
