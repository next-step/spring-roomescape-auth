package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.exception.UnauthorizedException;

@Service
public class LogoutService {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;

    public LogoutService(JwtTokenProvider jwtTokenProvider, CookieUtils cookieUtils) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtils = cookieUtils;
    }

    public Cookie logout(Cookie[] cookies) {
        String token = cookieUtils
                .getCookieByName(cookies, "token")
                .orElseThrow(() -> UnauthorizedException.of("쿠키에 토큰이 없습니다."))
                .getValue();
        boolean isInvalidToken = !jwtTokenProvider.validateToken(token);

        if (isInvalidToken) {
            throw UnauthorizedException.of("토큰이 만료되었습니다.");
        }
        return cookieUtils.deleteCookie("token");
    }
}
