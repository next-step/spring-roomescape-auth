package nextstep.reservation;

public class ReservationRequest {
    private Long scheduleId;
    private Long memberId;

    public ReservationRequest() {
    }

    public ReservationRequest(Long scheduleId, Long memberId) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
