package nextstep.reservation;

import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;
    private String name;

    public Reservation(Long id, Theme theme, LocalDate date, LocalTime time, String name) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public Reservation(Theme theme, LocalDate date, LocalTime time, String name) {
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public Long getId() {
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

    public String getName() {
        return name;
    }
}
