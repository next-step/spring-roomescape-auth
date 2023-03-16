package nextstep.core.reservation;

public class Reservation {
    private final Long id;
    private final Long scheduleId;
    private final Long memberId;

    public Reservation(Long scheduleId, Long memberId) {
        this(null, scheduleId, memberId);
    }

    public Reservation(Long id, Long scheduleId, Long memberId) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
    }

    public boolean isSameReservationId(Long id) {
        return this.id.equals(id);
    }

    public boolean isSameScheduleId(Long scheduleId) {
        return this.scheduleId.equals(scheduleId);
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

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", memberId=" + memberId +
                '}';
    }
}
