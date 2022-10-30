package nextstep.auth;

import nextstep.member.MemberRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = jwtTokenProvider.getPrincipalBy(request.getHeader("authorization"));
        List<String> roles = jwtTokenProvider.getRoles(token);

        if (roles.stream().noneMatch(it -> MemberRole.ADMIN.name().equals(it))) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return true;
    }
}
