package nextstep.reservation;

public class ReservationRequest {
    private Long scheduleId;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
