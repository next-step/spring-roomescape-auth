package roomescape.application.domain.reservation.service.command;

import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;

import java.time.LocalDate;

public class CreateReservationCommand {

    private final String name;

    private final LocalDate date;

    private final Long timeId;

    private final Long themeId;

    public CreateReservationCommand(
            String name,
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public ReservationName fetchReservationName() {
        return new ReservationName(this.name);
    }

    public ReservationTimeId fetchReservationTimeId() {
        return new ReservationTimeId(this.timeId);
    }

    public ThemeId fetchThemeId() {
        return new ThemeId(this.themeId);
    }

    public ReservationDate fetchReservationDate() {
        return new ReservationDate(this.date);
    }
}
