package nextstep.app.web.reservation;

import nextstep.core.reservation.in.ReservationCreateRequest;

record ReservationCreateWebRequest(Long scheduleId) {
    public ReservationCreateRequest to() {
        return new ReservationCreateRequest(scheduleId);
    }
}
