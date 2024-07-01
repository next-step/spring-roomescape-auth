package roomescape.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.RoleType;
import roomescape.exception.custom.UnauthorizedAccessException;
import roomescape.util.CookieUtils;
import roomescape.util.JwtTokenProvider;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthorizationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request == null ? null : request.getCookies();
        String token = CookieUtils.extractCookieValue(cookies, TOKEN);

        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        String role = claims.get(ROLE, String.class);

        if (!RoleType.fromName(role).isAdmin()) {
            throw new UnauthorizedAccessException();
        }

        return true;
    }
}
