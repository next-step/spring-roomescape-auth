package nextstep.core.reservation.in;

import nextstep.core.reservation.Reservation;

public record ReservationCreateRequest(Long scheduleId) {

    public Reservation to(Long memberId) {
        return new Reservation(scheduleId, memberId);
    }
}
