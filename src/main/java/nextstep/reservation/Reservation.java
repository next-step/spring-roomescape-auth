package nextstep.reservation;

import java.util.Objects;
import nextstep.auth.AuthenticationException;
import nextstep.schedule.Schedule;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Long memberId;
    private String name;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Long memberId, String name) {
        this(null, schedule, memberId, name);
    }

    public Reservation(Long id, Schedule schedule, Long memberId, String name) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
        this.name = name;
    }

    public void validateOwner(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new AuthenticationException();
        }
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}
