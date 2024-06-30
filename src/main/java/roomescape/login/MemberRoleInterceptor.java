package roomescape.login;

import static roomescape.util.CookieUtils.getCookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.error.exception.AuthenticationException;
import roomescape.member.MemberRole;

public class MemberRoleInterceptor implements HandlerInterceptor {

    private final List<String> excludeGetUrls = List.of("/themes", "/times", "/times/available");

    private JwtTokenProvider jwtTokenProvider;

    public MemberRoleInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        if(isExcludeToCheckRole(request)) {
            return true;
        }
        String token = getCookie(request, "token")
            .orElseThrow(AuthenticationException::new)
            .getValue();

        if(isAdmin(token)) {
            return true;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    private boolean isExcludeToCheckRole(HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        return method.equals("GET") && excludeGetUrls.contains(requestURI);
    }

    private boolean isAdmin(String token) {
        MemberRole role = jwtTokenProvider.getMemberRole(token);

        return role.equals(MemberRole.ADMIN);
    }
}
