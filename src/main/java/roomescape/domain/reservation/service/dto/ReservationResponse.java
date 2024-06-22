package roomescape.domain.reservation.service.dto;

import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

public class ReservationResponse {

    private final Long id;
    private final String name;
    private final String date;
    private final Time time;
    private final Theme theme;
    private final Member member;

    public ReservationResponse(Long id, String name, String date, Time time, Theme theme, Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
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

    public Member getMember() {
        return member;
    }
}
