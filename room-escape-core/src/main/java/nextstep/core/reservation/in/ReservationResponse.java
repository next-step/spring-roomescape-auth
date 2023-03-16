package nextstep.core.reservation.in;

import nextstep.core.reservation.Reservation;

public record ReservationResponse(Long id, Long scheduleId, Long memberId) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getScheduleId(), reservation.getMemberId());
    }
}
