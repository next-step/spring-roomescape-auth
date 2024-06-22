package roomescape.exception;

public enum ErrorCode {

	// @formatter:off
	INVALID_PARAMETER("RES-1001", "유효하지 않은 값입니다."),
	INVALID_TIME("RES-1001", "유효하지 않은 시간 값입니다."),
	INVALID_RESERVATION_NAME("RES-1002", "예약자명을 입력해주세요."),
	INVALID_THEME_NAME("RES-1003", "테마 이름을 입력해주세요."),
	INVALID_THEME_DESCRIPTION("RES-1004", "테마에 대한 설명을 입력해주세요."),
	INVALID_THEME_THUMBNAIL("RES-1005", "테마에 대한 썸네일 URL 을 입력해주세요."),
	INVALID_EMAIL("RES-1006", "이메일 형식이 올바르지 않습니다."),
	INVALID_PASSWORD("RES-1007", "비밀번호가 틀립니다."),

	DUPLICATE_RESERVATION("RES-2001", "중복 예약은 불가능합니다."),
	PAST_RESERVATION("RES-2002", "지나간 날짜와 시간에 대한 예약 생성은 불가능합니다."),
	DUPLICATE_MEMBER("RES-2003", "이미 가입한 계정입니다."),
	DUPLICATE_THEME_NAME("RES-2004", "이미 등록된 이름입니다."),

	NOT_FOUND_RESERVATION("RES-3001", "해당 예약이 없습니다."),
	NOT_FOUND_RESERVATION_TIME("RES-3002", "해당 예약 시간이 없습니다."),
	NOT_FOUND_THEME("RES-3003", "해당 테마가 없습니다."),
	NOT_FOUND_MEMBER("RES-3004", "등록되지 않은 사용자입니다."),

	EXPIRED_LOGIN_TOKEN("RES-4001", "로그인 토큰이 만료되었습니다."),
	NEEDS_LOGIN("RES-4002", "로그인이 필요합니다."),
	FORBIDDEN("RES-4003", "접근 권한이 없습니다.");
	// @formatter:on

	private final String code;

	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

}
