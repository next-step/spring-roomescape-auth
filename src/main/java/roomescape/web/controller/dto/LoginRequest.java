package roomescape.web.controller.dto;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import org.springframework.util.ObjectUtils;

public record LoginRequest(String email, String password) {

	public static void validateLoginInfo(LoginRequest request) {
		if (ObjectUtils.isEmpty(request.email()) || ObjectUtils.isEmpty(request.password())) {
			throw new RoomEscapeException(ErrorCode.INVALID_PARAMETER);
		}
	}
}
