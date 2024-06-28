package roomescape.member.infra;

import jakarta.servlet.http.Cookie;

public class TokenUtil {

    public static String extractTokenFromCookie(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException("토큰 정보가 없습니다.");
    }
}
