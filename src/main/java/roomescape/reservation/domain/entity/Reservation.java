package roomescape.reservation.domain.entity;

import roomescape.member.domain.entity.Member;
import roomescape.reservationtime.domain.entity.ReservationTime;
import roomescape.theme.domain.entity.Theme;

public class Reservation {
    private final Long id;
    private final Member member;
    private final String date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, Member member, String date, ReservationTime time, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(Long id, Member member, String date, ReservationTime time, Theme theme) {
        return new Reservation(id, member, date, time, theme);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
