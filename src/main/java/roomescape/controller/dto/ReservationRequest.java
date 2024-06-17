package roomescape.controller.dto;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import org.springframework.util.ObjectUtils;

public record ReservationRequest(String name, String date, long timeId, long themeId) {

	public static void validateReservation(ReservationRequest request) {
		if (ObjectUtils.isEmpty(request.name())) {
			throw new RoomEscapeException(ErrorCode.INVALID_RESERVATION_NAME);
		}

		if (request.date() == null) {
			throw new RoomEscapeException(ErrorCode.INVALID_TIME);
		}
	}

}
