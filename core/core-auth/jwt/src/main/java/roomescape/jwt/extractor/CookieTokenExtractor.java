package roomescape.jwt.extractor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.jwt.JwtToken;

import java.util.Objects;

@Component
public class CookieTokenExtractor {

    private static final String TOKEN_COOKIE_KEY = "token";

    public JwtToken extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return new JwtToken("cookie is null");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_KEY)) {
                return new JwtToken(cookie.getValue());
            }
        }

        return new JwtToken("");
    }
}
