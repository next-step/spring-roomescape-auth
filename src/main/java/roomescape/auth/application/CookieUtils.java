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

    public Optional<Cookie> getCookieByName(Cookie[] cookies, String name) {
        if (cookies == null) {
            throw UnauthorizedException.of("쿠키에 토큰이 없습니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }
}
