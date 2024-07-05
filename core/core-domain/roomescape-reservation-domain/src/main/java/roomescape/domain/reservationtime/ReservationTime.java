package roomescape.domain.reservationtime;

import roomescape.domain.reservationtime.validator.ReservationTimeValidator;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;

import java.time.LocalTime;

public class ReservationTime {

    private final ReservationTimeId id;

    private final ReservationTimeStartAt startAt;

    public ReservationTime(
            ReservationTimeId id,
            ReservationTimeStartAt startAt
    ) {
        ReservationTimeValidator.validate(id, startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public String getFormatStartAt(String pattern) {
        return startAt.getFormat(pattern);
    }

    public ReservationTimeId getId() {
        return id;
    }

    public ReservationTimeStartAt getStartAt() {
        return startAt;
    }

    public LocalTime getStartAtValue() {
        return this.startAt.startAt();
    }
}
