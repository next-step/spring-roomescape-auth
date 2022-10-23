package nextstep.auth;

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
        String token = jwtTokenProvider.extractToken(request);
        jwtTokenProvider.validateToken(token);
        checkAdmin(token);
        return true;
    }

    private void checkAdmin(String token) {
        if (!jwtTokenProvider.getRole(token).equals("ADMIN")) {
            throw new AuthenticationException("관리자만 이용가능합니다");
        }
    }
}
