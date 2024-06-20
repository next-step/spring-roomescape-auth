package roomescape.domain.reservation.domain;

import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

import static roomescape.utils.DateTimeCheckUtil.isBeforeCheck;
import static roomescape.utils.FormatCheckUtil.reservationDateFormatCheck;
import static roomescape.utils.FormatCheckUtil.reservationNameFormatCheck;

public class Reservation {

    private Long id;
    private String name;
    private String date;
    private Time time;
    private Theme theme;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        validationCheck(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private static void validationCheck(String name, String date, Time time) {
        reservationNameFormatCheck(name);
        reservationDateFormatCheck(date);
        isBeforeCheck(date, time.getStartAt());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
