package roomescape.web.controller.dto;

import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(long timeId, String startAt, boolean alreadyBooked) {

	public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
		return new AvailableReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(),
				alreadyBooked);
	}
}
