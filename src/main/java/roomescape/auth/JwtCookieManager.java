package roomescape.auth;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;

public final class JwtCookieManager {

	private static final String COOKE_PATH = "/";

	private static final String TOKEN = "token";

	private JwtCookieManager() {
		throw new IllegalStateException("Utility class");
	}

	public static Cookie createCookie(String token, int maxAge) {
		Cookie cookie = new Cookie(TOKEN, token);
		cookie.setHttpOnly(true);
		cookie.setPath(COOKE_PATH);
		cookie.setMaxAge(maxAge);
		return cookie;
	}

	public static Cookie clearCookie() {
		Cookie cookie = new Cookie(TOKEN, null);
		cookie.setHttpOnly(true);
		cookie.setPath(COOKE_PATH);
		cookie.setMaxAge(0);
		return cookie;
	}

	public static String extractTokenFromCookie(Cookie[] cookies) {
		if (cookies == null || cookies.length == 0) {
			return "";
		}

		return Arrays.stream(cookies)
			.filter((cookie) -> TOKEN.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse("");
	}

}
