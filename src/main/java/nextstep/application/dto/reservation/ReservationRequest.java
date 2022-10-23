package nextstep.application.dto.reservation;

public class ReservationRequest {

    private Long scheduleId;

    private ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
