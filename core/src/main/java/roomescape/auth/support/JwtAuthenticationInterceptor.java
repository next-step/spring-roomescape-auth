package roomescape.auth.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.dto.LoginToken;
import roomescape.auth.exception.UnauthorizedException;

import java.util.Arrays;
import java.util.Optional;

/**
 * JWT 인증을 처리하는 인터셉터.
 * 모든 경로에 대해 JWT 인증을 수행하고,
 * - 인증이 필요 없는 경로인 경우 인증하지 않는다.
 * - 인증이 필요한 경로인 경우 JWT 토큰을 검증한다.
 * - JWT 인증에 실패한 경우 401 Unauthorized 응답을 반환한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final SecurityPathConfig securityPathConfig;
    private final JwtSupporter jwtSupporter;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String requestPath = request.getRequestURI();

        // 인증이 필요 없는 경로인 경우 인증하지 않는다
        if (securityPathConfig.isPublicPath(requestPath)) {
            return true;
        }

        final Optional<LoginToken> loginTokenOpt = extractLoginToken(request);
        if (loginTokenOpt.isEmpty()) {
            throw new UnauthorizedException("Empty LoginToken in request");
        }

        if (!jwtSupporter.isValidToken(loginTokenOpt.get())) {
            throw new UnauthorizedException("LoginToken is invalid");
        }

        return true;
    }

    private Optional<LoginToken> extractLoginToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .map(cookie -> new LoginToken(cookie.getValue()));
    }
}
