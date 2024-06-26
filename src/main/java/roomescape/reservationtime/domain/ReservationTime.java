package roomescape.reservationtime.domain;

import java.util.Objects;

public class ReservationTime {

    private final static ReservationTimePolicy reservationTimePolicy = new ReservationTimePolicy();

    private Long timeId;
    private String startAt;

    public ReservationTime(Long timeId) {
        this.timeId = timeId;
    }

    public ReservationTime(String startAt) {
        if (reservationTimePolicy.validateStartAt(startAt)) {
            throw new IllegalArgumentException("예약 시간 형식이 올바르지 않습니다.");
        }
        this.startAt = startAt;
    }

    public ReservationTime(Long timeId, String startAt) {
        this.timeId = timeId;
        if (reservationTimePolicy.validateStartAt(startAt)) {
            throw new IllegalArgumentException("예약 시간 형식이 올바르지 않습니다.");
        }
        this.startAt = startAt;
    }

    public Long getTimeId() {
        return timeId;
    }

    public String getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(timeId, that.timeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeId);
    }

    @Override
    public String toString() {
        return "ReservationTime{" +
                "id=" + timeId +
                ", startAt='" + startAt + '\'' +
                '}';
    }
}
