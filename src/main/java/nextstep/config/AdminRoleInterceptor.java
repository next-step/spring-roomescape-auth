package nextstep.config;

import nextstep.auth.AuthenticationException;
import nextstep.auth.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminRoleInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("authorization");
        if (!jwtTokenProvider.validateToken(authorizationHeader)) {
            throw new AuthenticationException();
        }
        List<String> roles = jwtTokenProvider.getRoles(authorizationHeader);
        return roles.contains("ADMIN");
    }
}
