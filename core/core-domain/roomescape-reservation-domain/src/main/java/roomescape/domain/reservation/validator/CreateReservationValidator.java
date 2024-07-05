package roomescape.domain.reservation.validator;

import roomescape.domain.reservation.ReservationDateTime;
import roomescape.domain.reservation.vo.CreateReservationTime;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.error.exception.CreateReservationValidateException;
import roomescape.error.exception.DomainValidateException;

import java.util.Objects;

public class CreateReservationValidator {

    private final ReservationDate date;
    private final ReservationTime time;
    private final CreateReservationTime createTime;

    public CreateReservationValidator(ReservationDate date, ReservationTime time, CreateReservationTime createTime) {
        this.date = date;
        this.time = time;
        this.createTime = createTime;
    }

    public void validate() {
        this.validateNotNull(date, time, createTime);
        this.validateFuture(date, time, createTime);
    }

    private void validateNotNull(
            ReservationDate date,
            ReservationTime time,
            CreateReservationTime createTime
    ) {
        if (Objects.isNull(date)) {
            throw DomainValidateException.notToBeNull(ReservationDate.class);
        }
        if (Objects.isNull(time)) {
            throw DomainValidateException.notToBeNull(ReservationTime.class);
        }
        if (Objects.isNull(createTime)) {
            throw DomainValidateException.notToBeNull(CreateReservationTime.class);
        }
    }

    private void validateFuture(ReservationDate date, ReservationTime time, CreateReservationTime createTime) {
        if (new ReservationDateTime(date, time).isBeforeThanCreateTime(createTime)) {
            throw CreateReservationValidateException.pastTime(time);
        }
    }
}
