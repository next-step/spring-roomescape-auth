package roomescape.util;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;

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
        validateCookies(cookies, cookieName);

        String cookieValue = "";
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }

    private static void validateCookies(Cookie[] cookies, String cookieName) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키가 존재하지 않습니다.");
        }

        if (StringUtils.isEmpty(cookieName)) {
            throw new IllegalArgumentException("쿠키명이 올바르지 않습니다.");
        }
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
