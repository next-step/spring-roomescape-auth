package nextstep.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationExtractor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationExtractor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long extractClaims(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !(authorization.contains("Bearer"))) {
            throw new AuthenticationException();
        }

        String token = authorization.replace("Bearer ", "").trim();
        Long userId = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        return userId;
    }
}
