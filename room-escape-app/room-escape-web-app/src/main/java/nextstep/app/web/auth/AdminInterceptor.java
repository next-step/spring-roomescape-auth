package nextstep.app.web.auth;

import nextstep.core.member.MemberRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider tokenProvider;
    private final AuthorizationResolver authorizationResolver;

    public AdminInterceptor(JwtTokenProvider tokenProvider, AuthorizationResolver authorizationResolver) {
        this.tokenProvider = tokenProvider;
        this.authorizationResolver = authorizationResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authorizationResolver.resolve(request.getHeader("authorization"));
        List<String> roles = tokenProvider.getRoles(token);

        if (roles.contains(MemberRole.ADMIN.name())) {
            return true;
        }
        throw new AuthenticationException("관리자만 접근할 수 있습니다.");
    }
}
