package roomescape.reservationtime.ui.dto;

import roomescape.reservationtime.domain.entity.ReservationTime;

import java.util.List;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public static List<ReservationTimeResponse> fromReservationTimes(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream().map(ReservationTimeResponse::from).toList();
    }
}
