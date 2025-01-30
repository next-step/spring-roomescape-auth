package roomescape.domain.reservationtime.application.response;

import roomescape.domain.reservationtime.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record AvailableReservationTimeResponse(
        Long timeId,
        LocalTime startAt
) {

    public static AvailableReservationTimeResponse from(ReservationTime time) {
        return new AvailableReservationTimeResponse(
                time.getId().value(),
                time.getStartAt()
        );
    }

    public static List<AvailableReservationTimeResponse> from(final List<ReservationTime> times) {
        return times.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
    }
}
