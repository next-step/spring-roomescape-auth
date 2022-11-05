package nextstep.auth;

import nextstep.auth.AuthService;
import nextstep.auth.AuthenticationException;
import nextstep.member.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authService.extractToken(request);
        Role role = authService.getRole(token);
        if (!role.isAdmin()) {
            throw new AuthenticationException("어드민 권한이 존재하지 않습니다.");
        }
        return true;
    }
}