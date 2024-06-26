package roomescape.reservation;

import roomescape.reservationTime.ReservationTime;
import roomescape.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private Long id;

    private Long memberId;

    private LocalDate date;

    private ReservationTime reservationTime;

    private Theme theme;

    public Reservation(Long id, Long memberId, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.memberId = memberId;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Long memberId, String date, ReservationTime reservationTime, Theme theme) {
        this(null, memberId, LocalDate.parse(date), reservationTime, theme);
    }

    public boolean isBeforeThanNow() {
        return LocalDateTime.of(date, reservationTime.getStartAt()).isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

}
