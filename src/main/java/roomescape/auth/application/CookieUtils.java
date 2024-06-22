package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

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
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }
}
