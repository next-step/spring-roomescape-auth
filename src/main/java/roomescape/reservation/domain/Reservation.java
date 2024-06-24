package roomescape.reservation.domain;

import java.time.LocalDate;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;

public class Reservation {

    private Long id;
    private LocalDate date;
    private User user;
    private ReservationTime time;
    private Theme theme;

    protected Reservation() {
    }

    public Reservation(LocalDate date, User user, ReservationTime time, Theme theme) {
        this(null, date, user, time, theme);
    }

    public Reservation(Long id, String date, User user, ReservationTime time, Theme theme) {
        this(id, LocalDate.parse(date), user, time, theme);
    }

    public Reservation(Long id, LocalDate date, User user, ReservationTime time, Theme theme) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
