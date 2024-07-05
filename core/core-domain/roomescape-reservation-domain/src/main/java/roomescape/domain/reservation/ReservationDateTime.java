package roomescape.domain.reservation;

import roomescape.domain.reservation.vo.CreateReservationTime;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;

import java.time.LocalDateTime;

public class ReservationDateTime {

    private final ReservationDate date;

    private final ReservationTime time;

    public ReservationDateTime(ReservationDate date, ReservationTime time) {
        this.date = date;
        this.time = time;
    }

    public boolean isBeforeThanCreateTime(CreateReservationTime createTime) {
        return LocalDateTime.of(date.date(), time.getStartAtValue()).isBefore(createTime.time());
    }
}
