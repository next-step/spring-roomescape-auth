package roomescape.apply.reservationtime.ui.dto;

import roomescape.apply.reservationtime.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        long timeId,
        String startAt,
        boolean alreadyBooked
) {
    public static AvailableReservationTimeResponse from(ReservationTime saved, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(saved.getId(), saved.getStartAt(), alreadyBooked);
    }
}
