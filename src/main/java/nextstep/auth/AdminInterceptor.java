package nextstep.auth;

import nextstep.member.MemberRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(AuthorizationExtractor authorizationExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = authorizationExtractor.extractAccessToken(request);
        if (!(jwtTokenProvider.validateToken(accessToken))) {
            throw new AuthenticationException();
        }
        MemberRole role = jwtTokenProvider.getRole(accessToken);
        if (role.isAdmin()) {
            return true;
        }
        throw new AuthenticationException();
    }
}
