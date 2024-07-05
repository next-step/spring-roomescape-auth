package roomescape.application.domain.reservationtime.service.query;

import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.theme.vo.ThemeId;

import java.time.LocalDate;

public class FindAvailableTimesQuery {

    private final LocalDate date;

    private final Long themeId;

    public FindAvailableTimesQuery(LocalDate date, Long themeId) {
        this.date = date;
        this.themeId = themeId;
    }

    public ReservationDate fetchReservationDate() {
        return new ReservationDate(date);
    }

    public ThemeId fetchThemeId() {
        return new ThemeId(themeId);
    }
}
