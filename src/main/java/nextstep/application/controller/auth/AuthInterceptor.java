package nextstep.application.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.application.service.auth.JwtTokenProvider;
import nextstep.common.exception.AuthException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        String token = extractToken(request);

        if (jwtTokenProvider.validateToken(token)) {
            request.setAttribute("principal", jwtTokenProvider.getPrincipal(token));
            request.setAttribute("roles", jwtTokenProvider.getRoles(token));
            return true;
        }
        return false;
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization.startsWith("Bearer")) {
            return authorization.split(" ")[1];
        }
        throw new AuthException("유효하지 않은 토큰입니다.");
    }
}
