package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.error.exception.AuthenticationException;
import roomescape.member.MemberRole;

public class MemberRoleInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public MemberRoleInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            throw new AuthenticationException();
        }

        String token = Arrays.stream(cookies)
            .filter(cookie -> "token".equals(cookie.getName()))
            .findFirst()
            .orElseThrow(AuthenticationException::new)
            .getValue();

        MemberRole role = jwtTokenProvider.getMemberRole(token);

        if(!role.equals(MemberRole.ADMIN)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
