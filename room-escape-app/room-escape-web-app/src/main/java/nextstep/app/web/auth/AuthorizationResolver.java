package nextstep.app.web.auth;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationResolver {
    private static final String TOKEN_FORMAT = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    public AuthorizationResolver(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String resolve(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException("토큰 정보가 없습니다.");
        }
        if (!authorization.startsWith(TOKEN_FORMAT)) {
            throw new AuthenticationException("잘못된 토큰 형식입니다.");
        }

        String token = authorization.replace(TOKEN_FORMAT, "");
        if (!tokenProvider.validateToken(token)) {
            throw new AuthenticationException("잘못된 토큰입니다.");
        }
        return token;
    }
}
