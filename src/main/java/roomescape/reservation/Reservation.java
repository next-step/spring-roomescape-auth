package roomescape.reservation;

import roomescape.member.Member;
import roomescape.reservationTime.ReservationTime;
import roomescape.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private Long id;

    private Member member;

    private LocalDate date;

    private ReservationTime reservationTime;

    private Theme theme;

    public Reservation(Long id, Member member, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Member member, String date, ReservationTime reservationTime, Theme theme) {
        this(null, member, LocalDate.parse(date), reservationTime, theme);
    }

    public boolean isBeforeThanNow() {
        return LocalDateTime.of(date, reservationTime.getStartAt()).isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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
