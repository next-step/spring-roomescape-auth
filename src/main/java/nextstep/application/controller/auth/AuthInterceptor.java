package nextstep.application.controller.auth;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.application.service.auth.JwtTokenProvider;
import nextstep.common.Role;
import nextstep.common.exception.AuthException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(
        AuthExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        String token = authExtractor.extract(request);
        List<String> roles = jwtTokenProvider.getRoles(token);

        if (isAdmin(roles)) {
            return true;
        }
        throw new AuthException("권한이 없습니다. 관리자만 접근할 수 있습니다.");
    }

    private boolean isAdmin(List<String> roles) {
        return roles.stream()
            .anyMatch(role -> Role.ADMIN.name().equals(role));
    }
}
