package roomescape.domain.reservation;

import roomescape.domain.reservation.validator.CreateReservationValidator;
import roomescape.domain.reservation.validator.ReservationValidator;
import roomescape.domain.reservation.vo.CreateReservationTime;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;

public class Reservation {

    private final ReservationId id;

    private final ReservationName name;

    private final ReservationDate date;

    private final ReservationTimeId timeId;

    private final ThemeId themeId;


    private Reservation(
            ReservationId id,
            ReservationName name,
            ReservationDate date,
            ReservationTimeId timeId,
            ThemeId themeId
    ) {
        this.date = date;
        ReservationValidator.validate(id, name, timeId, themeId);
        this.timeId = timeId;
        this.themeId = themeId;
        this.id = id;
        this.name = name;
    }

    public static Reservation create(
            ReservationId id,
            ReservationName name,
            ReservationDate date,
            ReservationTime time,
            ThemeId themeId,
            CreateReservationTime createTime
    ) {
        CreateReservationValidator validator = new CreateReservationValidator(date, time, createTime);
        validator.validate();

        return new Reservation(id, name, date, time.getId(), themeId);
    }

    public static Reservation of(
            ReservationId id,
            ReservationName name,
            ReservationDate date,
            ReservationTimeId timeId,
            ThemeId themeId
    ) {
        return new Reservation(id, name, date, timeId, themeId);
    }

    public ReservationId getId() {
        return this.id;
    }

    public ReservationName getName() {
        return this.name;
    }

    public ReservationTimeId getTimeId() {
        return this.timeId;
    }

    public ThemeId getThemeId() {
        return this.themeId;
    }

    public String getFormatDate(String pattern) {
        return date.getFormat(pattern);
    }

    public ReservationDate getDate() {
        return date;
    }
}
