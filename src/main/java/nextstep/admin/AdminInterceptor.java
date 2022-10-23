package nextstep.admin;

import static nextstep.member.MemberRole.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.AuthenticationException;
import nextstep.auth.JwtTokenProvider;
import nextstep.member.MemberRole;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String token = parseToken(request);
        MemberRole role = jwtTokenProvider.getRole(token);

        if (role == ADMIN) {
            return true;
        } else {
            throw new AuthenticationException();
        }
    }

    private String parseToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).split("Bearer ")[1];
    }
}
