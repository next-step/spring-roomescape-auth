package nextstep.app.web.reservation;

import nextstep.core.reservation.in.ReservationResponse;

class ReservationWebResponse {
    private Long id;
    private Long scheduleId;
    private Long memberId;

    private ReservationWebResponse() {
    }

    private ReservationWebResponse(Long id, Long scheduleId, Long memberId) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
    }

    public static ReservationWebResponse from(ReservationResponse reservation) {
        return new ReservationWebResponse(reservation.id(), reservation.scheduleId(), reservation.memberId());
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
