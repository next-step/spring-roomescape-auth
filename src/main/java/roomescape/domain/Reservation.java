package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;
//    private User user;

    public Reservation() {
    }

    public Reservation(Long id, String userName, String date, Long timeId, String startAt, Long themeId, String themeName) {
        this.id = id;
        this.name = userName;
        this.date = LocalDate.parse(date);
        this.time = new ReservationTime(timeId, startAt);
        this.theme = new Theme(themeId, themeName);
    }

    public Reservation(Long id, String userName, String date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = userName;
        this.date = LocalDate.parse(date);
        this.time = new ReservationTime(time.getId(), time.getStartAt().toString());
        this.theme = new Theme(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
