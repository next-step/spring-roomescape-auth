package roomescape.web.controller.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import org.springframework.util.ObjectUtils;

public record MemberRequest(String name, String email, String password) {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public static void validateMember(MemberRequest request) {
		if (ObjectUtils.isEmpty(request.name()) || ObjectUtils.isEmpty(request.password())
				|| ObjectUtils.isEmpty(request.email())) {
			throw new RoomEscapeException(ErrorCode.INVALID_PARAMETER);
		}

		Matcher matcher = EMAIL_PATTERN.matcher(request.email());

		if (!matcher.matches()) {
			throw new RoomEscapeException(ErrorCode.INVALID_EMAIL);
		}
	}
}
