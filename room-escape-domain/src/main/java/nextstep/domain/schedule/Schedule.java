package nextstep.domain.schedule;

import nextstep.domain.Identity;
import nextstep.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Identity id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

    public Schedule(Identity id, Theme theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Identity getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public static Schedule empty() {
        return new Schedule(null, null, null, null);
    }
}
