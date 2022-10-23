package nextstep.auth;

import nextstep.auth.jwt.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (!request.getRequestURI().startsWith("/admin")) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }

            if (!isAdmin(request)) {
                response.setStatus(401);
                return false;
            }

        } catch (Exception ignored) {
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isAdmin(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            return jwtTokenProvider.validateToken(token) && jwtTokenProvider.getTokenBody(token).getRoles().contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
        return authorization.substring("Bearer".length()).trim();
    }
}
