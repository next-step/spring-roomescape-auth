package roomescape.util;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    private CookieUtils() {
    }

    public static ResponseCookie createResponseCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();
    }

    public static String extractCookieValue(Cookie[] cookies, String cookieName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static Cookie expireCookieByName(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    return cookie;
                }
            }
        }
        return null;
    }
}
