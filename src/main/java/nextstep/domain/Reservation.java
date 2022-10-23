package nextstep.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private Long id;
    private Long scheduleId;
    private Long memberId;

    private Reservation() {
    }

    public Reservation(Long scheduleId, Long memberId) {
        this(null, scheduleId, memberId);
    }

    public Reservation(Long id, Long scheduleId, Long memberId) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
