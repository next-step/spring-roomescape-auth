package roomescape.web.controller.dto;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import org.springframework.util.ObjectUtils;

public record ReservationSearchRequest(long memberId, long themeId, String dateFrom, String dateTo) {

	public static void validateSearchRequest(ReservationSearchRequest request) {
		if (ObjectUtils.isEmpty(request.memberId()) || ObjectUtils.isEmpty(request.themeId())
				|| ObjectUtils.isEmpty(request.dateFrom()) || ObjectUtils.isEmpty(request.dateTo())) {
			throw new RoomEscapeException(ErrorCode.INVALID_PARAMETER);
		}
	}

}
