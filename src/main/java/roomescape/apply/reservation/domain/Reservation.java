package roomescape.apply.reservation.domain;

import roomescape.apply.member.domain.MemberId;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.domain.Theme;

public class Reservation {

    private Long id;
    private String name;
    private String date;
    private ReservationTime time;
    private Theme theme;
    private MemberId memberId;

    protected Reservation() {

    }

    public Reservation(Long id, String name, String date, ReservationTime time, Theme theme, Long memberId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.memberId = MemberId.of(memberId);
    }

    public static Reservation of(String name, String date, ReservationTime time, Theme theme, Long memberId) {
        Reservation reservation = new Reservation();
        reservation.name = name;
        reservation.date = date;
        reservation.time = time;
        reservation.theme = theme;
        reservation.memberId = MemberId.of(memberId);
        return reservation;
    }

    public void changeId(long id) {
        this.id = id;
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

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getMemberId() {
        return memberId.longValue();
    }
}
