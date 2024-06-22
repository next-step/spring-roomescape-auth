package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;

import java.util.Optional;

@Component
public class CookieUtils {
    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Optional<Cookie> getOneCookieByName(Cookie[] cookies, String name) {
        if (cookies == null) {
            throw UnauthorizedException.of(name + "과 일치하는 쿠키가 없습니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
